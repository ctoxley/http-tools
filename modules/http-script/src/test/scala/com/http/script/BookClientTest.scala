package com.http.script

import com.http.script.JsonAsserts.{assertConflicted, assertOk}
import com.http.script.TestData.aBook
import com.http.script.client.{Book, BookClient}
import utest.{TestSuite, Tests, assert, test}

object BookClientTest extends TestSuite {

  override def utestAfterAll(): Unit = BookClient.deleteAll()

  override def utestBeforeEach(path: Seq[String]): Unit = BookClient.deleteAll()

  val tests: Tests = Tests {
    test("fail to get book") {
      assertBookNotPresent("not-present")
    }
    test("put and get book") {
      val book = aBook()
      BookClient.put(book)
      assertBookPresent(book)
    }
    test("post and get book") {
      val book = aBook()
      BookClient.post(book)
      assertBookPresent(book)
    }
    test("post twice expecting conflict") {
      val book = aBook()
      BookClient.post(book)
      val response = BookClient.post(book)
      assertConflicted(response, s"Resource with ID[${book.id}] already exists.")
    }
    test("delete book") {
      val book = aBook()
      BookClient.put(book)
      assertBookPresent(book)
      BookClient.delete(book.id)
      assertBookNotPresent(book.id)
    }
  }

  private def assertBookNotPresent(id: String): Unit = {
    val response = BookClient.get(id)
    assert(response.isNotFound)
  }

  private def assertBookPresent(book: Book): Unit = {
    val response = BookClient.get(book.id)
    assertOk(response, expectedBody = s"""{"id":"${book.id}","title":"${book.title}"}""")
  }
}
