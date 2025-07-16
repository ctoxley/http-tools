package com.http.script

import com.http.script.JsonAsserts.assertOk
import com.http.script.client.PostClient
import utest._

object PostClientTest extends TestSuite {

  val tests: Tests = Tests {
    test("Post to dev null") {
      assertOk(PostClient.postToDevNull)
    }
    test("Post matching body contains true") {
      assertOk(PostClient.postMatchingBody, """{"matched":true}""")
    }
  }
}
