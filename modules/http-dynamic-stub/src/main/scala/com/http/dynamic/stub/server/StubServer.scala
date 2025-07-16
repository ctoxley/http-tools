package com.http.dynamic.stub.server

import com.http.dynamic.stub.{Api, ApiInput}
import com.http.dynamic.stub.Http.Headers
import com.http.dynamic.stub.model.PathParts
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.{AbstractHandler, HandlerList}
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.slf4j.{LoggerFactory, MDC}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.jdk.CollectionConverters.*
import scala.util.Try

class StubServer {

  private val logger = LoggerFactory.getLogger(getClass)

  private val threadPool = new QueuedThreadPool
  threadPool.setName("DynamicStubServer")

  private val server = new Server(threadPool)
  server.setRequestLog(new CustomRequestLog(new Slf4jRequestLogWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT))

  private val handlers = new HandlerList
  handlers.addHandler(new AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit =
      MDC.put("requestId", extractHeaders(request).requestId)
  })

  private val services = ArrayBuffer[Api]()

  def importRoutes(service: Api): Unit = {
    val connector = new ServerConnector(server)
    connector.setPort(service.port)
    connector.setName(service.name)
    handlers.addHandler(new AbstractHandler() {
        def handle(target: String, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
          parsePath(target).foreach { pp =>
            handleRequest(pp, jettyRequest, request, response, service)
          }
        }
      })
    server.addConnector(connector)
    services += service
  }

  def start(): Unit = {
    handlers.addHandler(new AbstractHandler() {
      def handle(target: String, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.info("No route found for path[{}] with contentType[{}].", target, request.getContentType)
        Api.logAvailableServices(services.toSeq)
        response.setStatus(NOT_FOUND_404)
        jettyRequest.setHandled(true)
      }
    })
    server.setHandler(handlers)
    server.start()
  }

  def stop(): Unit = {
    server.stop()
  }

  private def handleRequest(path: PathParts, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse, api: Api): Unit = {
    if (jettyRequest.getLocalPort == api.port) {
      val input = ApiInput(request.getMethod, path, extractBody(request), extractHeaders(request))
      val output = api.router.handle(input)
      output.foreach { o =>
        response.setStatus(o.status)
        response.getWriter.print(o.body)
        jettyRequest.setHandled(true)
      }
    }
  }

  private def extractBody(request: HttpServletRequest): String =
    if (request.getContentLength == 0) "" else Source.fromInputStream(request.getInputStream).mkString

  private def extractHeaders(request: HttpServletRequest): Headers =
    Headers(request.getHeaderNames.asScala.toList.map(n => (n, request.getHeader(n))).toMap)

  private def parsePath(target: String): Option[PathParts] =
    Try(PathParts.from(target)).toOption match {
      case Some(pp) =>
        Some(pp)
      case _ =>
        logger.info("Incoming request's path[{}] can not be handled. Parse error.", target)
        None
    }
}