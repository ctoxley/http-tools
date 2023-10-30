package com.http.stub.dynamic.model

import utest.{TestSuite, Tests, assert, intercept, test}

object EndpointDefinitionTest extends TestSuite {

  val MethodGet = "GET"
  val MethodPost = "POST"
  val MethodPut = "PUT"
  val MethodDelete = "DELETE"

  val ContentTypeJson = "application/json"
  val ContentTypeAny = "*"

  val get = Get("/p1")
  val post = Post("/p1/*/p3")
  val put = Put("/p1/*")
  val delete = Delete("/*/p2")

  val tests = Tests {
    test("Get") {
      assert(get.method == MethodGet)
      assert(get.supports(MethodGet, ContentTypeAny))
    }
    test("Post") {
      assert(post.method == MethodPost)
      assert(post.supports(MethodPost, ContentTypeJson))
      assert(post.supports(MethodPost, ContentTypeAny) == false)
    }
    test("Put") {
      assert(put.method == MethodPut)
      assert(put.supports(MethodPut, ContentTypeJson))
      assert(put.supports(MethodPut, ContentTypeAny) == false)
    }
    test("Delete") {
      assert(delete.method == MethodDelete)
      assert(delete.supports(MethodDelete, ContentTypeAny))
    }
    test("Invalid path") {
      val requiredNotPresent = intercept[IllegalArgumentException] {
        Get("")
      }
      assert(requiredNotPresent.getMessage == "Unable to parse path[]. Paths must start with a `/`.")
    }
  }
}
