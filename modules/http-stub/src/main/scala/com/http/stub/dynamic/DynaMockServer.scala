package com.http.stub.dynamic

import com.http.stub.dynamic.model.PathParts
import com.http.stub.model.{DynaMockService, Service}
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.http.HttpHeader.CONTENT_TYPE
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON
import org.eclipse.jetty.server.handler.{AbstractHandler, HandlerList}
import org.eclipse.jetty.server.{Request, Server, ServerConnector}
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Try

case class DynaMockServer() {

  private val logger = LoggerFactory.getLogger(getClass)

  val threadPool = new QueuedThreadPool
  threadPool.setName("DynaMockServer")

  val server = new Server(threadPool)

  import org.eclipse.jetty.server.CustomRequestLog
  import org.eclipse.jetty.server.Slf4jRequestLogWriter

  server.setRequestLog(new CustomRequestLog(new Slf4jRequestLogWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT))

  val handlers = new HandlerList
  server.setHandler(handlers)

  var services = List.empty[DynaMockService]

  def importRoutes(service: DynaMockService) = {
    val connector = new ServerConnector(server)
    connector.setPort(service.port)
    connector.setName(service.name)
    handlers.addHandler(new AbstractHandler() {
        def handle(target: String, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
          parsePath(target) match {
            case Some(pp) => handleRequest(target, pp, jettyRequest, request, response, service)
            case _ => respondNoRouteFound(target, extractHeaders(request), jettyRequest, response)
          }
        }
      })
    server.addConnector(connector)
    services = service +: services
  }

  def start() = {
    server.start()
  }

  def stop = {
    server.stop()
  }

  private def handleRequest(target: String, path: PathParts, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse, service: DynaMockService) = {
    val requestSupported = service.router.supports(jettyRequest.getMethod, path, request.getContentType)
    if (requestSupported && jettyRequest.getLocalPort == service.port) {
      val input = Input(request.getMethod, path, extractBody(request))
      val output = service.router.handle(input)
      response.setStatus(output.status)
      response.getWriter.print(output.resource)
      jettyRequest.setHandled(true)
    } else {
      respondNoRouteFound(target, extractHeaders(request), jettyRequest, response)
    }
  }

  private def respondNoRouteFound(target: String, headers: String, jettyRequest: Request, response: HttpServletResponse) = {
    logger.info("No route found for path[{}] with headers[{}].", target, headers)
    response.setStatus(NOT_FOUND_404)
    val json = Service.asJson(services)
    response.setHeader(CONTENT_TYPE.name(), APPLICATION_JSON.name())
    response.getWriter.print(json)
    jettyRequest.setHandled(true)
  }

  private def extractBody(request: HttpServletRequest): String =
    if (request.getContentLength == 0) "" else Source.fromInputStream(request.getInputStream).mkString

  private def extractHeaders(request: HttpServletRequest): String =
    request.getHeaderNames.asScala.toList.map(n => s"$n=${request.getHeader(n)}").mkString(",")

  private def parsePath(target: String): Option[PathParts] =
    Try(PathParts.from(target)).toOption match {
      case Some(pp) =>
        logger.info("Incoming request's path[{}] has potential to be handled. Parsed to [{}].", target, pp)
        Some(pp)
      case _ =>
        logger.info("Incoming request's path[{}] can not be handled.", target)
        None
    }
}