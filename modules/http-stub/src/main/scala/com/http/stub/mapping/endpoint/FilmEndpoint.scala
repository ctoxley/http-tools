package com.http.stub.mapping.endpoint

import com.http.stub.dynamic.{HasIdentity, InMemoryStore, Input, Output}
import upickle.default.{ReadWriter, macroRW, read, write}

case class Film(id: String, name: String) extends HasIdentity[String]

class FilmEndpoint {

  val store = new InMemoryStore[String, Film]

  implicit val filmRW: ReadWriter[Film] = macroRW

  def get(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.get(id) match {
        case Some(f) => Output.ok(write(f))
        case _ => Output.notFound
      }
    case _ =>
      Output.notFound
  }

  def post(input: Input): Output = {
    val film = read[Film](input.body)
    store.store(film)
    Output.ok(input.body)
  }
}
