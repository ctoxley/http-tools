package com.http.dynamic.stub.route

import com.http.dynamic.stub.model.PathParts
import com.http.dynamic.stub.{ApiOutput, FixedTestData}
import utest.{TestSuite, Tests, assert, test}
import org.eclipse.jetty.http.HttpStatus.*

object SimpleJsonRouteTest extends TestSuite with FixedTestData {

  private val idInJson = "id"
  private val bodyItem1 = s"""{"id":"1"}"""
  private val bodyItem2 = s"""{"id":"2"}"""
  private val jsonItem1 = ujson.Obj("id" -> ujson.Str("1"))
  private val jsonItem2 = ujson.Obj("id" -> ujson.Str("2"))
  private val postInput = postJsonInput.copy(body = bodyItem1, json = jsonItem1)
  private val putInput = putJsonInput.copy(body = bodyItem1, json = jsonItem1)

  val tests: Tests = Tests {
    test("get") {
      test("get one, ok") {
        val route = SimpleJsonRoute(idInJson)
        route.post(postInput)
        val output = route.get(getTextInput)
        assert(output.status == OK_200)
        assert(output.body == bodyItem1)
      }
      test("get one, not found") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.get(getTextInput)
        assert(output.status == NOT_FOUND_404)
      }
      test("get one, missing ID path param") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.get(getTextInput.copy(path = PathParts.from("/item")))
        assertBadRequestMissingIdPathParam(output)
      }
    }
    test("post") {
      test("add one, ok") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.post(postInput)
        assert(output.status == OK_200)
        assert(output.body == bodyItem1)
      }
      test("add two, conflict") {
        val route = SimpleJsonRoute(idInJson)
        route.post(postInput)
        val output = route.post(postInput)
        assert(output.status == CONFLICT_409)
        assert(output.body == """{"error":{"message":"Resource with ID[1] already exists.","code":"resource-already-exists"}}""")
      }
      test("add one, bad request, missing ID") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.post(postInput.copy(body = "{}", json = ujson.Null))
        assertBadRequestMissingIdInBody(output)
      }
    }
    test("put") {
      test("put one") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.put(putInput)
        assert(output.status == OK_200)
        assert(output.body == bodyItem1)
      }
      test("put two") {
        val route = SimpleJsonRoute(idInJson)
        route.put(putInput.copy(body = "{}"))
        val output = route.put(putInput)
        assert(output.status == OK_200)
        assert(output.body == bodyItem1)
      }
      test("put with no path param ID") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.put(putInput.copy(path = PathParts.from("/item")))
        assertBadRequestMissingIdPathParam(output)
      }
      test("put with different path param ID to the body") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.put(putInput.copy(body = bodyItem2))
        assert(output.status == BAD_REQUEST_400)
        assert(output.body == """{"error":{"message":"Path param ID[1] does not match body ID[2].","code":"resource-id-mismatch"}}""")
      }
      test("put with no body ID") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.put(putInput.copy(body = "{}"))
        assertBadRequestMissingIdInBody(output)
      }
      test("put with no IDs") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.put(putInput.copy(path = PathParts.from("/item"), body = "{}"))
        assert(output.status == BAD_REQUEST_400)
        assert(output.body == """{"error":{"message":"ID path param and ID in body is missing.","code":"resource-id-missing"}}""")
      }
    }
    test("delete") {
      test("delete") {
        val route = SimpleJsonRoute(idInJson)
        route.post(postInput)
        val output = route.delete(deleteTextInput)
        assert(output.status == OK_200)
        assert(output.body == bodyItem1)
        assert(route.get(deleteTextInput).status == NOT_FOUND_404)
      }
      test("delete not found") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.delete(deleteTextInput)
        assert(output.status == NOT_FOUND_404)
      }
      test("delete missing ID path param") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.delete(deleteTextInput.copy(path = PathParts.from("/item")))
        assertBadRequestMissingIdPathParam(output)
      }
    }
    test("delete all") {
      val route = SimpleJsonRoute(idInJson)
      route.post(postInput)
      route.post(postInput.copy(body = bodyItem2))
      assert(route.get(deleteTextInput).status == OK_200)
      assert(route.get(deleteTextInput.copy(path = PathParts.from("/item/2"))).status == OK_200)
      val output = route.deleteAll(deleteTextInput)
      assert(route.get(deleteTextInput).status == NOT_FOUND_404)
      assert(route.get(deleteTextInput.copy(path = PathParts.from("/item/2"))).status == NOT_FOUND_404)
    }
    test("list all") {
      test("list all") {
        val route = SimpleJsonRoute(idInJson)
        route.post(postInput)
        route.post(postInput.copy(body = bodyItem2))
        val output = route.listAll(deleteTextInput)
        assert(output.status == OK_200)
        assert(output.body == """[{"id":"1"},{"id":"2"}]""")
      }
      test("list nothing") {
        val route = SimpleJsonRoute(idInJson)
        val output = route.listAll(deleteTextInput)
        assert(output.status == OK_200)
        assert(output.body == """[]""")
      }
    }
  }

  private def assertBadRequestMissingIdPathParam(output: ApiOutput): Unit =
    assert(output.body == """{"error":{"message":"ID path param is missing.","code":"resource-id-missing"}}""")
    assert(output.status == BAD_REQUEST_400)

  private def assertBadRequestMissingIdInBody(output: ApiOutput): Unit =
    assert(output.body == """{"error":{"message":"ID in body is missing.","code":"resource-id-missing"}}""")
    assert(output.status == BAD_REQUEST_400)
}
