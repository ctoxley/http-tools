package com.http.script

import utest._

object GetClientTest extends TestSuite {

  val tests = Tests {
    test("Get file") {
      val response = GetClient.getFile
      assert(response.text.startsWith("""{"name":"get-file","""))
    }
    test("Get empty") {
      val response = GetClient.getEmpty
      assert(response.text == "{}")
    }
    test("Get with two params") {
      val response = GetClient.getWithTwoPathParams("one", "two")
      assert(response.text == """{"param1":"one","param2":"two"}""")
    }
    test("Get with two params") {
      val response = GetClient.getWithQueryParamsListOfTwo("one", "two")
      assert(response.text == """{"list":"one,two"}""")
    }
    test("Get only match request if name set to true") {
      val response = GetClient.getWithQueryParamMatchTrueOnly
      assert(response.text == """{"name":"true"}""")
    }
  }
}
