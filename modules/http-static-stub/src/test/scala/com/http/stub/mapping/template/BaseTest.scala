package com.http.stub.mapping.template

import utest.{TestSuite, Tests, test, assert}

object BaseTest extends TestSuite with Base {

  val tests: Tests = Tests {
    test("path param") {
      assert(firstPathParam == "{{request.path.[0]}}")
      assert(secondPathParam == "{{request.path.[1]}}")
      assert(thirdPathParam == "{{request.path.[2]}}")
      assert(fourthPathParam == "{{request.path.[3]}}")
      assert(fifthPathParam == "{{request.path.[4]}}")
    }
    test("query param, index") {
      assert(firstQueryParam("key") == "{{request.query.key.0}}")
      assert(secondQueryParam("key") == "{{request.query.key.1}}")
      assert(thirdQueryParam("key") == "{{request.query.key.2}}")
      assert(fourthQueryParam("key") == "{{request.query.key.3}}")
      assert(fifthQueryParam("key") == "{{request.query.key.4}}")
    }
    test("query param") {
      assert(queryParam("key") == "{{request.query.key}}")
    }
    test("JSON response") {
      val response = aJsonResponse(200).build()
      assert(response.getStatus == 200)
      assert(response.getHeaders.getHeader(contentType).containsValue(applicationJson))
      assert(response.getTransformers.contains("response-template"))
    }
  }
}
