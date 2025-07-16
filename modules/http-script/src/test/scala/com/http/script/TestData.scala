package com.http.script

import com.http.script.client.Book

import java.util.concurrent.atomic.AtomicLong

object TestData {

  private val sequence: AtomicLong = new AtomicLong()
  private def nextId: Long = sequence.incrementAndGet()

  def aBook(): Book = {
    val id = nextId
    Book(s"$id", s"name$id")
  }
}
