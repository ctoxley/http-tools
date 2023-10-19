package com.http.script

import utest.{TestSuite, Tests, assert, intercept, test}

object JsonResponseTest  extends TestSuite {

  val tests = Tests {
    test("Get") {
      val response = GetClient.getFile
      assert(response("name").str == "get-file")
      assert(response.safeStrOpt("name") == Some("get-file"))
      assert(response.safeStrOpt("notPresent") == None)
      assert(response.getObjByPath("level-1.level-2.level-3")("name").str == "level-3")
    }
    test("Validation") {
      val response = GetClient.getFile

      val requiredNotPresent = intercept[AssertionError] {
        response.required("notPresent")
      }
      assert(requiredNotPresent.getMessage.startsWith(s"Required field[notPresent] not present in JSON[${response.text}]."))

      val requiredPresentButNull = intercept[AssertionError] {
        response.required("i-am-null")
      }
      assert(requiredPresentButNull.getMessage.startsWith(s"Required field[i-am-null] present in JSON[${response.text}] but value null."))

      val assertNotEqual = intercept[AssertionError] {
        response.assert("name", "incorrect-value")
      }
      assert(assertNotEqual.getMessage.startsWith(s"Field[name] contains unexpected value[get-file] as expected[incorrect-value]."))

      assert(response.assert("name", "get-file"))

    }
  }
}
