package com.http.stub.dynamic

import com.http.stub.dynamic.model._
import org.slf4j.LoggerFactory

case class Router(routes: Routes) {

  private val logger = LoggerFactory.getLogger(getClass)

  def handle(input: Input): Option[Output] = {
    val handlerKey = findFirstMatchingEndpoint(input)
    handlerKey match {
      case Some(k) =>
        logger.info("Router found endpoint[{}] given input[{}].", k, input)
        Some(routes(k)(input))
      case _ =>
        logger.info("Router could not find endpoint given input[{}]. Tried {}", input, routes.keys)
        None
    }
  }

  private def findFirstMatchingEndpoint(input: Input): Option[EndpointDefinition] =
    routes.keys
      .filter(_.supports(input.method, input.headers.contentType))
      .filter(_.pathParts.length == input.path.length)
      .filter(matchesPath(input.path, _))
      .headOption

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