package com.http.script

import com.http.script.JsonResponse.ValueOpts
import com.http.script.client.{FilmClient, GetClient}
import utest._

object JsonResponseTest  extends TestSuite {

  val response: JsonResponse = GetClient.getFile

  val tests: Tests = Tests {
    test("Response status codes") {
      GetClient.getFile.is2xx ==> true
      GetClient.getFile.isOk ==> true
      FilmClient.get("notPresent").isNotFound ==> true
      JsonResponse(ujson.Null, 204).isNoContent ==> true
      JsonResponse(ujson.Null, 400).isBadRequest ==> true
    }
    test("Get") {
      response.navigateToValue("name").str ==> "get-file"
      response.navigateToValue("notPresent").strOpt ==> None
      response.navigateToValue("id").num ==> 123456789
      response.navigateToValue("notPresent").numOpt ==> None
      response.navigateToValue("level-1", "name").str ==> "level-1"
      response.navigateToValue("level-1", "name").str ==> "level-1"
      response.navigateToValue("level-1", "level-2a", "level-3", "name").str ==> "level-3"
      response.navigateToValue("notPresent") ==> ujson.Null
      response.navigateToValue("level-1.level-2.notPresent") ==> ujson.Null
    }
    test("Is not present, null or empty") {
      response.navigateToValue("notPresent") ==> ujson.Null
      response.navigateToValue("level-1", "level-2", "notPresent").isNull ==> true
      response.navigateToValue("i-am-null").isNull ==> true
      response.navigateToValue("level-1", "level-2a", "notPresent").isEmpty ==> true
      response.navigateToValue("empty").isEmpty ==> true
      response.navigateToValue("has-no-elements").isEmpty ==> true
    }
    test("Is present, not null or not empty") {
      response.navigateToValue("name").isNotNull ==> true
      response.navigateToValue("name").isNotEmpty ==> true
      response.navigateToValue("level-1", "level-2a", "level-3", "name").isNotNull ==> true
      response.navigateToValue("level-1", "level-2a", "level-3", "name").isNotEmpty ==> true
      response.navigateToValue("empty").isNotNull ==> true
      response.navigateToValue("empty").isNotEmpty ==> false
      response.navigateToValue("has-no-elements").isNotEmpty ==> false
      response.navigateToValue("sub-documents").isNotEmpty ==> true
    }
    test("Find value by name and value") {
      val objName = ujson.Str("sub-document-2")
      val obj = response.navigateToValue("sub-documents").firstValue("name", objName)
      obj ==> ujson.Obj("name" -> objName)
    }
    test("Find value by name") {
      val obj = response.navigateToValue("level-1").firstValue("level-2a")
      obj.isNotNull ==> true
      obj("name").str ==> "level-2a"
    }
    test("Length") {
      response.navigateToValue("sub-documents").length ==> 3
      response.navigateToValue("level-1").length ==> 0
    }
  }
}
