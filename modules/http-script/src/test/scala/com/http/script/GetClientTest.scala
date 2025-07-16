package com.http.script

import com.http.script.JsonAsserts.assertOk
import com.http.script.client.GetClient
import utest._

object GetClientTest extends TestSuite {

  val tests: Tests = Tests {
    test("Get file") {
      val response = GetClient.getFile
      assert(response.text.contains("My first message."))
    }
    test("Get empty") {
      val response = GetClient.getEmpty
      assert(response.text == "{}")
    }
    test("Get with two params") {
      val response = GetClient.getWithTwoPathParams("one", "two")
      assertOk(response, """{"param1":"one","param2":"two"}""")
    }
    test("Get with two params") {
      val response = GetClient.getWithQueryParamsListOfTwo("one", "two")
      assertOk(response, """{"list":"one,two"}""")
    }
    test("Get only match request if name set to true") {
      val response = GetClient.getWithQueryParamMatchTrueOnly
      assertOk(response, """{"name":"true"}""")
    }
    test("Get path param and query param") {
      val response = GetClient.getPathParamAndQueryParam("pathParam", "queryParam")
      assertOk(response, """{"pathParam":"pathParam","queryParam":"queryParam"}""")
    }
  }
}