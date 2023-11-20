package com.http.stub.dynamic.model

import utest.{TestSuite, Tests, assert, intercept, test}

object EndpointDefinitionTest extends TestSuite {

  val ContentTypeAny = Some("*")

  val get = Get("/p1")
  val post = Post("/p1/*/p3")
  val put = Put("/p1/*")
  val delete = Delete("/*/p2")

  val tests = Tests {
    test("Get") {
      assert(get.method == Http.Get)
      assert(get.supports(Http.Get, ContentTypeAny))
    }
    test("Post") {
      assert(post.method == Http.Post)
      assert(post.supports(Http.Post, Some(Http.Json)))
      assert(post.supports(Http.Post, ContentTypeAny) == false)
    }
    test("Put") {
      assert(put.method == Http.Put)
      assert(put.supports(Http.Put, Some(Http.Json)))
      assert(put.supports(Http.Put, ContentTypeAny) == false)
    }
    test("Delete") {
      assert(delete.method == Http.Delete)
      assert(delete.supports(Http.Delete, ContentTypeAny))
    }
    test("Invalid path") {
      val requiredNotPresent = intercept[IllegalArgumentException] {
        Get("")
      }
      assert(requiredNotPresent.getMessage == "Unable to parse path[]. Paths must start with a `/`.")
    }
    test("Headers") {
      test("Content type") {
        val headers = Headers(Map("Content-Type" -> "text"))
        assert(headers.contentType == Some("text"))
        assert(Headers.Empty.contentType == None)
        assert(headers.hasContentTypeJson == false)
      }
      test("Request ID") {
        val headers = Headers(Map("X-Request-ID" -> "ID"))
        assert(headers.requestId == "ID")
        assert(Headers.Empty.requestId.isEmpty == false)
      }
      test("Content type JSON") {
        val headers = Headers(Map("Content-Type" -> Http.Json))
        assert(headers.hasContentTypeJson)
      }
    }
  }
}
