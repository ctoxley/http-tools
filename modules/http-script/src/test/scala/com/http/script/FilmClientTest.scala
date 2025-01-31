package com.http.script

import com.http.script.JsonAsserts.assertConflicted
import com.http.script.TestData.aFilm
import com.http.script.client.{Film, FilmClient}
import utest.{TestSuite, Tests, assert, test}

object FilmClientTest  extends TestSuite {

  override def utestAfterAll(): Unit = FilmClient.deleteAll

  val tests: Tests = Tests {
    test("fail to get film") {
      assertFilmNotPresent("not-present")
    }
    test("put and get film") {
      val film = aFilm()
      FilmClient.put(film)
      assertFilmPresent(film)
    }
    test("post and get film") {
      val film = aFilm()
      FilmClient.post(film)
      assertFilmPresent(film)
    }
    test("post twice expecting conflict") {
      val film = aFilm()
      FilmClient.post(film)
      val response = FilmClient.post(film)
      assertConflicted(response, expectedMessage = s"Film with ID[${film.id}] already exists.")
    }
    test("delete film") {
      val film = aFilm()
      FilmClient.post(film)
      assertFilmPresent(film)
      FilmClient.delete(film.id)
      assertFilmNotPresent(film.id)
    }
  }

  private def assertFilmNotPresent(id: String): Unit = {
    val response = FilmClient.get(id)
    assert(response.isNotFound)
  }

  private def assertFilmPresent(film: Film): Unit = {
    val response = FilmClient.get(film.id)
    assert(response.is2xx)
    assert(response.text == s"""{"id":"${film.id}","name":"${film.name}"}""")
  }
}
