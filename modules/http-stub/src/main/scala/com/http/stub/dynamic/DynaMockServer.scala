package com.http.stub.dynamic

import com.http.stub.dynamic.model.{Headers, PathParts}
import com.http.stub.model.{DynaMockService, Service}
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.eclipse.jetty.server.handler.{AbstractHandler, HandlerList}
import org.eclipse.jetty.server.{Request, Server, ServerConnector}
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.slf4j.{LoggerFactory, MDC}

import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Try

case class DynaMockServer() {

  private val logger = LoggerFactory.getLogger(getClass)

  val threadPool = new QueuedThreadPool
  threadPool.setName("DynaMockServer")

  val server = new Server(threadPool)

  import org.eclipse.jetty.server.{CustomRequestLog, Slf4jRequestLogWriter}

  server.setRequestLog(new CustomRequestLog(new Slf4jRequestLogWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT))

  val handlers = new HandlerList
  handlers.addHandler(new AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit =
      MDC.put("requestId", extractHeaders(request).requestId)
  })

  var services = List.empty[DynaMockService]

  def importRoutes(service: DynaMockService) = {
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
    services = service +: services
  }

  def start() = {
    handlers.addHandler(new AbstractHandler() {
      def handle(target: String, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
        logger.info("No route found for path[{}] with headers[{}] contentType[{}].", target, extractHeaders(request).headers, request.getContentType)
        logger.info("Services offer: {}", Service.asJson(services))
        response.setStatus(NOT_FOUND_404)
        jettyRequest.setHandled(true)
      }
    })
    server.setHandler(handlers)
    server.start()
  }

  def stop = {
    server.stop()
  }

  private def handleRequest(path: PathParts, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse, service: DynaMockService) = {
    if (jettyRequest.getLocalPort == service.port) {
      val input = Input(request.getMethod, path, extractBody(request), extractHeaders(request))
      val output = service.router.handle(input)
      output.foreach { o =>
        response.setStatus(o.status)
        response.getWriter.print(o.resource)
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