package com.http.script

import com.http.script.TestData.aFilm
import com.http.script.client.{Film, FilmClient}
import requests.RequestFailedException
import utest.{TestSuite, Tests, assert, intercept, test}

object FilmClientTest  extends TestSuite {

  val tests = Tests {
    test("fail to get film") {
      val film = aFilm()
      assertFilmNotPresent(film)
    }
    test("post and get film") {
      val film = aFilm()
      FilmClient.post(film)
      assertFilmPresent(film)
    }
    test("delete film") {
      val film = aFilm()
      FilmClient.post(film)
      assertFilmPresent(film)
      FilmClient.delete(film.id)
      assertFilmNotPresent(film)
    }
  }

  private def assertFilmNotPresent(film: Film): Unit = {
    val notFound = intercept[RequestFailedException] {
      FilmClient.get(film.id)
    }
    assert(notFound.response.is4xx)
  }

  private def assertFilmPresent(film: Film): Unit = {
    val response = FilmClient.get(film.id)
    assert(response.statusOfSuccess)
    assert(response.text == s"""{"id":"${film.id}","name":"${film.name}"}""")
  }
}
