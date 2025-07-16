package com.http.dynamic.stub

import com.http.dynamic.stub.ApiOutput.ErrorCode
import com.http.dynamic.stub.Http.HttpEndpoint
import com.http.dynamic.stub.model.{EndpointInput, InvalidInput}
import org.slf4j.LoggerFactory

case class Router(routes: Routes, inputValidator: InputValidator = new InputValidator) {

  private val logger = LoggerFactory.getLogger(getClass)

  def handle(input: ApiInput): Option[ApiOutput] =
    val httpEndpoint = routes.keys.find(_.supports(input))
    httpEndpoint match
      case Some(e) =>
        Some(handleWith(e, input))
      case _ =>
        logger.info("Router could not find endpoint given input[{}].", input)
        logger.info("Tried {}", routes.keys.mkString(","))
        None

  private def handleWith(endpoint: HttpEndpoint, input: ApiInput) =
    inputValidator.validate(input) match {
      case i: EndpointInput =>
        logger.info("Router found endpoint[{}] given input[{}].", endpoint, input)
        routes(endpoint)(i)
      case InvalidInput(error) =>
        logger.info("Invalid input[{}] with error[{}].", input, error)
        ApiOutput.badRequest(
          message = s"Invalid input[$input] with error[$error].",
          code = ErrorCode.RequestMalformed
        )
    }
}