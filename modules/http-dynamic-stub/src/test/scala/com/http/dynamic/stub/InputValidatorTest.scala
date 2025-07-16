package com.http.dynamic.stub

import com.http.dynamic.stub.model.{TextInput, JsonInput, InvalidInput}
import utest.{TestSuite, Tests, assert, test}

object InputValidatorTest extends TestSuite with FixedTestData {

  private val inputValidator = new InputValidator
  private val jsonStr = """{"name":"value"}"""

  val tests: Tests = Tests {
    test("empty body") {
      val input = anApiInput().copy(body = "text")
      inputValidator.validate(input) match {
        case TextInput(method, path, body) =>
          assert(method == input.method)
          assert(path == input.path)
          assert(body == "text")
        case _ => assert(false)
      }
    }
    test("empty json body") {
      val input = anApiInput().copy(body = "", headers = applicationJsonHeaders)
      inputValidator.validate(input) match {
        case InvalidInput(error) =>
          assert(error == "JSON missing")
        case _ => assert(false)
      }
    }
    test("valid json input") {
      val input = anApiInput().copy(body = jsonStr, headers = applicationJsonHeaders)
      inputValidator.validate(input) match {
        case JsonInput(method, path, body, json) =>
          assert(method == input.method)
          assert(path == input.path)
          assert(body == jsonStr)
          assert(json == ujson.Obj.from(Map("name" -> ujson.Str("value"))))
        case _ => assert(false)
      }
    }
    test("invalid json input") {
      val input = anApiInput().copy(body = "notJson", headers = applicationJsonHeaders)
      inputValidator.validate(input) match {
        case InvalidInput(error) =>
          assert(error == "JSON parse error: expected null got 'n' at index 0")
        case _ => assert(false)
      }
    }
  }
}
