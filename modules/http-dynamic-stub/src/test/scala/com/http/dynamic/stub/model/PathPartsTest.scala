package com.http.dynamic.stub.model

import utest.{TestSuite, Tests, assert, test, intercept}

object PathPartsTest extends TestSuite {

  val tests: Tests = Tests {
    test("matches level 1") {
      val path1 = PathParts("p1", Seq.empty)
      assert(path1.matches(path1))
    }
    test("matches level 1 no match") {
      val path1 = PathParts("p1", Seq.empty)
      val path2 = PathParts("p2", Seq.empty)
      assert(!path1.matches(path2))
    }
    test("matches level 1 wildcard") {
      val path1 = PathParts("*", Seq.empty)
      val path2 = PathParts("p1", Seq.empty)
      assert(path1.matches(path2))
    }
    test("matches level 2") {
      val path1 = PathParts("l1", Seq("p1"))
      assert(path1.matches(path1))
    }
    test("matches level 2 no match") {
      val path1 = PathParts("l1", Seq("p1"))
      val path2 = PathParts("l1", Seq("p2"))
      assert(!path1.matches(path2))
    }
    test("matches level 2 wildcard") {
      val path1 = PathParts("l1", Seq("*"))
      val path2 = PathParts("l1", Seq("p1"))
      assert(path1.matches(path2))
    }
    test("matches level 2 and 1 no match") {
      val path1 = PathParts("l1", Seq("p1"))
      val path2 = PathParts("l2", Seq("p1"))
      assert(!path1.matches(path2))
    }
    test("matches level 2 and 1 wildcard") {
      val path1 = PathParts("*", Seq("p1"))
      val path2 = PathParts("l1", Seq("p1"))
      assert(path1.matches(path2))
    }
    test("to string") {
      val pathParts = PathParts.from("/1/2/3")
      assert(pathParts.toString == "/1/2/3")
    }
    test("length") {
      val pathParts = PathParts.from("/1/2/3")
      assert(pathParts.length == 3)
    }
    test("first") {
      val pathParts = PathParts.from("/1/2/3")
      assert(pathParts.first == "1")
    }
    test("second") {
      val pathParts = PathParts.from("/1/2/3")
      assert(pathParts.second.contains("2"))
    }
    test("third") {
      val pathParts = PathParts.from("/1/2/3")
      assert(pathParts.third.contains("3"))
    }
    test("second missing") {
      val pathParts = PathParts.from("/1")
      assert(pathParts.second.isEmpty)
    }
    test("third missing") {
      val pathParts = PathParts.from("/1")
      assert(pathParts.third.isEmpty)
    }
    test("invalid path") {
      val invalidPathException = intercept[IllegalArgumentException] {
        PathParts.from("invalid/")
      }
      assert(invalidPathException.getMessage == "Unable to parse path[invalid/]. Paths must start with a `/` and have least one part.")
    }
  }
}
