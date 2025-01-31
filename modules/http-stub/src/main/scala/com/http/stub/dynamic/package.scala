package com.http.stub

import com.http.stub.dynamic.model.{EndpointDefinition, Headers, PathParts}
import org.eclipse.jetty.http.HttpStatus._

package object dynamic {

  type Endpoint = Input => Output
  type Routes = Map[EndpointDefinition, Endpoint]

  case class Input(method: String, path: PathParts, body: String, headers: Headers) {
    override def toString: String = s"method:$method,path:$path,contentType:${headers.contentType}"
  }

  case class Output(status: Int, resource: String)

  object Output {
    val ok = Output(NO_CONTENT_204, "")
    val notFound = Output(NOT_FOUND_404, "")
    val badRequest = Output(BAD_REQUEST_400, "")

    def ok(body: String): Output = Output(OK_200, body)
    def conflict(message: String, code: String = "resource-already-exists"): Output =
      Output(CONFLICT_409, s"""{"error":{"message":"$message","code":"$code"}}""")
  }
}
