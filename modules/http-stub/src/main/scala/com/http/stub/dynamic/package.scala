package com.http.stub

import com.http.stub.dynamic.model.{EndpointDefinition, PathParts}
import org.eclipse.jetty.http.HttpStatus._

package object dynamic {

  type Endpoint = Input => Output
  type Routes = Map[EndpointDefinition, Endpoint]

  case class Input(method: String, path: PathParts, body: String)

  case class Output(status: Int, resource: String)

  object Output {

    def ok(body: String): Output = Output(OK_200, body)
    def notFound: Output = Output(NOT_FOUND_404, "")
  }
}
