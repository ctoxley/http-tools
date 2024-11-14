package com.http.script

import com.http.script.JsonResponse.{isEqualByPath, isPresentAndNotNull, isPresentAndNotNullByPath}
import com.http.script.client.{FilmClient, GetClient}
import utest.{TestSuite, Tests, assert, test}

object JsonResponseTest  extends TestSuite {

  val response = GetClient.getFile

  val tests = Tests {
    test("Response status codes") {
      assert(GetClient.getFile.statusOfSuccess)
      assert(GetClient.getFile.statusOk)
      assert(FilmClient.get("notPresent").statusNotFound)
    }
    test("Get") {
      assert(response("name").str == "get-file")
      assert(response.fieldOpt("name") == Some("get-file"))
      assert(response.fieldOpt("notPresent") == None)
      assert(response.navigateTo("level-1").map(_.apply("name").str) == Some("level-1"))
      assert(response.navigateTo("level-1.level-2.level-3").map(_.apply("name").str) == Some("level-3"))
      assert(response.navigateTo("notPresent") == None)
      assert(response.navigateTo("level-1.level-2.notPresent") == None)
    }
    test("Equal by path not present") {
      assert(isEqualByPath("notPresent", "nothing", response) == false)
    }
    test("Equal by path null value") {
      assert(isEqualByPath("i-am-null", "nothing", response) == false)
    }
    test("Equal by path") {
      assert(isEqualByPath("level-1.level-2.name", "level-2", response))
    }
    test("Is present, not present") {
      assert(isPresentAndNotNull("notPresent", response) == false)
      assert(isPresentAndNotNullByPath("level-1.level-2.notPresent", response) == false)
    }
    test("Is present, null value") {
      assert(isPresentAndNotNull("i-am-null", response) == false)
    }
    test("Is present") {
      assert(isPresentAndNotNull("name", response))
      assert(isPresentAndNotNullByPath("level-1.level-2.level-3.name", response))
    }
  }
}
