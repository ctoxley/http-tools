package com.http.script

import com.http.script.client.{Book, Film}

import java.util.concurrent.atomic.AtomicLong

object TestData {

  private val sequence = new AtomicLong()
  private def nextId = sequence.incrementAndGet()

  def aBook(): Book = {
    val id = nextId
    Book(s"$id", s"name$id")
  }

  def aFilm(): Film = {
    val id = nextId
    Film(s"$id", s"name$id")
  }
}
