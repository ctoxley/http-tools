package com.http.dynamic.stub

import com.http.dynamic.stub.model.PathParts
import com.http.dynamic.stub.route.BookRoutes
import utest.{TestSuite, Tests, test, assert}
import org.eclipse.jetty.http.HttpStatus.*

object RouterTest extends TestSuite with FixedTestData {

  private val book1Json = s"""{"id":"1","name":"book1"}"""
  private val book2Json = s"""{"id":"2","name":"book2"}"""
  private val getBook = getApiInput.copy(path = PathParts.from("/book/1"))
  private val deleteBook = deleteApiInput.copy(path = PathParts.from("/book/1"))
  private val postBook = postApiInput.copy(path = PathParts.from("/book"), body = book1Json)
  private val putBook = putApiInput.copy(path = PathParts.from("/book/1"), body = book1Json)

  val tests: Tests = Tests {
    test("get all book") {
      val router = Router(BookRoutes.all())
      router.handle(postBook)
      router.handle(postBook.copy(body = book2Json))
      val output = router.handle(getBook.copy(path = PathParts.from("/books")))
      assert(output.map(_.status).contains(OK_200))
      assert(output.map(_.body).contains(s"""[$book1Json,$book2Json]"""))
    }
    test("delete all book") {
      val router = Router(BookRoutes.all())
      router.handle(postBook)
      router.handle(postBook.copy(body = book2Json))
      router.handle(deleteBook.copy(path = PathParts.from("/books")))
      val output = router.handle(getBook.copy(path = PathParts.from("/books")))
      assert(output.map(_.status).contains(OK_200))
      assert(output.map(_.body).contains("[]"))
    }
    test("get book, not found") {
      val router = Router(BookRoutes.all())
      val output = router.handle(getBook)
      assert(output.map(_.status).contains(NOT_FOUND_404))
    }
    test("post and get book") {
      val router = Router(BookRoutes.all())
      router.handle(postBook)
      val output = router.handle(getBook)
      assert(output.map(_.status).contains(OK_200))
      assert(output.map(_.body).contains(book1Json))
    }
    test("put and get book") {
      val router = Router(BookRoutes.all())
      router.handle(putBook)
      val output = router.handle(getBook)
      assert(output.map(_.status).contains(OK_200))
      assert(output.map(_.body).contains(book1Json))
    }
    test("put and delete and get book") {
      val router = Router(BookRoutes.all())
      router.handle(putBook)
      router.handle(deleteBook)
      val output = router.handle(getBook)
      assert(output.map(_.status).contains(NOT_FOUND_404))
    }
  }
}
