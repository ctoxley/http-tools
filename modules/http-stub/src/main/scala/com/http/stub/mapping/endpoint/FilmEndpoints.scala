package com.http.stub.mapping.endpoint

import com.http.stub.dynamic.{HasIdentity, TypedInMemoryStore, Input, Output}
import upickle.default.{ReadWriter, macroRW, read, write}

case class Film(id: String, name: String) extends HasIdentity[String]

class FilmEndpoints(store: TypedInMemoryStore[String, Film]) {

  implicit val filmRW: ReadWriter[Film] = macroRW

  def deleteAll(input: Input) = {
    store.reset
    Output.ok
  }

  def listAll(input: Input) =
    if (store.allValues.isEmpty) Output.ok("[]")
    else Output.ok(store.allValues.mkString("[", ",", "]"))

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
    store.get(film.id) match {
      case Some(f) => Output.conflict(s"Film with ID[${f.id}] already exists.")
      case _ =>
        store.store(film)
        Output.ok(input.body)
    }
  }

  def put(input: Input): Output = {
    val film = read[Film](input.body)
    store.store(film)
    Output.ok(input.body)
  }

  def delete(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.delete(id) match {
        case Some(f) => Output.ok(write(f))
        case _ => Output.notFound
      }
    case _ =>
      Output.notFound
  }
}
