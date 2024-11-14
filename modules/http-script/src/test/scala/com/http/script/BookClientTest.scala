package com.http.script

import com.http.script.JsonResponse.isEqual
import com.http.script.TestData.aBook
import com.http.script.client.{Book, BookClient}
import requests.RequestFailedException
import utest.{TestSuite, Tests, assert, intercept, test}

object BookClientTest  extends TestSuite {

  val tests = Tests {
    test("fail to get book") {
      val book = aBook
      assertBookNotPresent(book)
    }
    test("put and get book") {
      val book = aBook
      BookClient.put(book)
      assertBookPresent(book)
    }
    test("post and get book") {
      val book = aBook
      BookClient.post(book)
      assertBookPresent(book)
    }
    test("post twice expecting conflict") {
      val book = aBook
      BookClient.post(book)
      val response = BookClient.post(book)
      assert(response.statusConflicted)
      assert(isEqual("error.message", s"Book with ID[${book.id}] already exists.", response))
    }
    test("delete book") {
      val book = aBook
      BookClient.put(book)
      assertBookPresent(book)
      BookClient.delete(book.id)
      assertBookNotPresent(book)
    }
  }

  private def assertBookNotPresent(book: Book): Unit = {
    val notFound = intercept[RequestFailedException] {
      BookClient.get(book.id)
    }
    assert(notFound.response.is4xx)
  }

  private def assertBookPresent(book: Book): Unit = {
    val response = BookClient.get(book.id)
    assert(response.statusOfSuccess)
    assert(response.text == s"""{"id":"${book.id}","title":"${book.title}"}""")
  }
}
