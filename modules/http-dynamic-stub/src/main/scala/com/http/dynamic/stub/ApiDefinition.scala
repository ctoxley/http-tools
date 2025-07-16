package com.http.dynamic.stub

import com.http.dynamic.stub.ApiOutput.ErrorCode.ResourceAlreadyExists
import com.http.dynamic.stub.Http.{Headers, HttpEndpoint}
import com.http.dynamic.stub.model.{EndpointInput, PathParts}
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod.*
import org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON
import org.eclipse.jetty.http.HttpStatus.*
import org.slf4j.LoggerFactory

import java.util.UUID

type Route = EndpointInput => ApiOutput
type Routes = Map[HttpEndpoint, Route]

final case class ApiInput(method: String, path: PathParts, body: String, headers: Headers) {
  override def toString: String = s"method:$method,path:$path,contentType:${headers.contentType.getOrElse("")}"
}

final case class ApiOutput(status: Int, body: String)

object ApiOutput {

  val okNoContent: ApiOutput = ApiOutput(NO_CONTENT_204, "")

  def notFound(body: String = ""): ApiOutput = ApiOutput(NOT_FOUND_404, body)

  def badRequest(message: String, code: ErrorCode): ApiOutput =
    ApiOutput(BAD_REQUEST_400, errorJson(message, code))

  def ok(body: String): ApiOutput = ApiOutput(OK_200, body)

  def created(body: String): ApiOutput = ApiOutput(CREATED_201, body)

  def conflict(message: String, code: ErrorCode = ResourceAlreadyExists): ApiOutput =
    ApiOutput(CONFLICT_409, errorJson(message, code))

  private def errorJson(message: String, code: ErrorCode) = s"""{"error":{"message":"$message","code":"${code.value}"}}"""

  enum ErrorCode(val value: String) {
    case ResourceAlreadyExists extends ErrorCode("resource-already-exists")
    case RequestMalformed extends ErrorCode("request-malformed")
    case ResourceIdMissing extends ErrorCode("resource-id-missing")
    case ResourceIdMismatch extends ErrorCode("resource-id-mismatch")
  }
}

final case class Api(name: String, host: String = "localhost", port: Int, router: Router)

object Api {

  private val logger = LoggerFactory.getLogger(getClass)

  def logAvailableServices(service: Seq[Api]): Unit = {
    service.foreach { s =>
      logger.info("Service {} on {}:{} has routes:", s.name, s.host, s.port)
      s.router.routes.keys.foreach { e =>
        logger.info("-> {}[{}].", e.method, e.pathParts)
      }
    }
  }
}

object Http {

  trait HttpEndpoint {
    self =>

    val path: String
    val pathParts: PathParts = PathParts.from(path)

    lazy val method: String = this match {
      case _: Get => GET.name()
      case _: Post => POST.name()
      case _: Put => PUT.name()
      case _: Delete => DELETE.name()
    }

    def supports(input: ApiInput): Boolean =
      self.method.equalsIgnoreCase(input.method) && self.pathParts.matches(input.path)
  }

  trait HttpEndpointWithBody extends HttpEndpoint {

    override def supports(input: ApiInput): Boolean =
      super.supports(input) && input.headers.hasContentTypeJson
  }

  final case class Get(path: String) extends HttpEndpoint
  final case class Post(path: String) extends HttpEndpointWithBody
  final case class Put(path: String) extends HttpEndpointWithBody
  final case class Delete(path: String) extends HttpEndpoint

  final case class Headers(headers: Map[String, String] = Map.empty) {

    val contentType: Option[String] = headers.get(Http.Header.contentType)
    val hasContentTypeJson: Boolean = contentType.contains(Http.MediaType.applicationJson)
    val requestId: String = headers.getOrElse("X-Request-ID", UUID.randomUUID().toString)
  }

  object Header {

    val requestId: String = "X-Request-ID"
    val contentType: String = HttpHeader.CONTENT_TYPE.asString()
  }

  object MediaType {

    val applicationJson: String = APPLICATION_JSON.getContentTypeField.getValue
  }
}