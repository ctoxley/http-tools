package com.http.script

import ujson.Value
import utest.{TestSuite, Tests, intercept, test}

object ValueTest extends TestSuite {

  val json = ujson.Obj("field1" -> "value1", "field2" -> ujson.Null)

  val tests = Tests {
    test("missing field") {
      val error = intercept[NoSuchElementException] {
        json("noPresent")
      }
      assert(error.getMessage == "key not found: noPresent")
    }
    test("field is null") {
      val field2 = json("field2")
      assert(field2 == ujson.Null)
    }
    test("field is null, try get str") {
      val error = intercept[Value.InvalidData] {
        json("field2").str
      }
      assert(error.getMessage == "Expected ujson.Str (data: null)")
    }
  }
}
