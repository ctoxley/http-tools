package com.http.script

import utest._

object PostClientTest extends TestSuite {

  val tests = Tests {
    test("Post to dev null") {
      val response = PostClient.postToDevNull
      assert(response.isSuccess)
    }
    test("Post matching body contains true") {
      val response = PostClient.postMatchingBody
      assert(response.text == """{"matched":true}""")
    }
  }
}
