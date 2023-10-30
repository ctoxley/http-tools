package com.http.stub.dynamic

import com.http.stub.dynamic.model._
import com.http.stub.model.Service
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import org.slf4j.LoggerFactory

case class Router(routes: Routes) {

  private val logger = LoggerFactory.getLogger(getClass)

  // check router can handle input using supports before calling handle.
  def handle(input: Input): Output = {
    val handlerKey = findFirstMatchingHandlerKey(input.path)
    handlerKey match {
      case Some(k) =>
        logger.info("Router found endpoint[{}] given path[{}].", k, input.path)
        routes(k)(input)
      case _ =>
        logger.info("Router could not find endpoint for path[{}].", input.path)
        Output(NOT_FOUND_404, Service.asJson(routes))
    }
  }

  def supports(httpMethod: String, path: PathParts, contentType: String): Boolean = {
    routes.find {
      case (e, _) => e.supports(httpMethod, contentType) && findFirstMatchingHandlerKey(path).isDefined
    }.isDefined
  }

  private def findFirstMatchingHandlerKey(path: PathParts): Option[EndpointDefinition] =
    routes.keys.filter(_.pathParts.length == path.length).filter(matchesPath(path, _)).headOption

  private def matchesPath(path: PathParts, endpoint: EndpointDefinition): Boolean = path match {
    case PathPartsThree(p1, p2, p3) =>
      endpoint.pathParts.matchesFirst(p1) &&
      endpoint.pathParts.matchesSecond(p2) &&
      endpoint.pathParts.matchesThird(p3)
    case PathPartsTwo(p1, p2) =>
      endpoint.pathParts.matchesFirst(p1) &&
      endpoint.pathParts.matchesSecond(p2)
    case PathPartsOne(p1) =>
      endpoint.pathParts.matchesFirst(p1)
  }
}