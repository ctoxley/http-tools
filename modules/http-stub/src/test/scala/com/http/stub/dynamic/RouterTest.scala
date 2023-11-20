package com.http.stub.dynamic

import com.http.stub.dynamic.model._
import com.http.stub.model.Service
import org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404
import utest.{TestSuite, Tests, test, assert}

object RouterTest extends TestSuite {

  val headersWithJsonContentType = Headers(Map(Http.ContentType -> Http.Json))

  val inputGet = Input(Http.Get, PathPartsTwo("resource", "1"), "", Headers.Empty)
  val inputPost = Input(Http.Post, PathPartsOne("resource"), "{}", headersWithJsonContentType)
  val inputPostTwoPath = Input(Http.Post, PathPartsOne("resource/field"), "{}", headersWithJsonContentType)
  val inputDelete = Input(Http.Delete, PathPartsTwo("resource", "1"), "{}", Headers.Empty)
  val inputPut = Input(Http.Put, PathPartsTwo("resource", "1"), "{}", headersWithJsonContentType)

  val routes = Map[EndpointDefinition, Endpoint](
    Get("/resource/*") -> (_ => outputGet),
    Post("/resource") -> (_ => outputPost),
    Delete("/resource/*") -> (_ => outputDelete),
  )

  val outputGet = Output.ok("GET")
  val outputPost = Output.ok("POST")
  val outputDelete = Output.ok("DELETE")
  val outputNotFound = Output(NOT_FOUND_404, Service.asJson(routes))

  val router = Router(routes)

  val tests = Tests {
    test("Routes to the right endpoint") {
      assert(router.handle(inputGet) == outputGet)
      assert(router.handle(inputPost) == outputPost)
      assert(router.handle(inputDelete) == outputDelete)
      assert(router.handle(inputPostTwoPath) == outputNotFound)
    }
  }
}
