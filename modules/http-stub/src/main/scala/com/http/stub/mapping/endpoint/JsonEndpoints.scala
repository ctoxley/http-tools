package com.http.stub.mapping.endpoint

import com.http.stub.dynamic.{Input, Output, SimpleInMemoryStore}

class JsonEndpoints(id: String) {

  val store = new SimpleInMemoryStore[String, String]

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
        case Some(d) => Output.ok(d)
        case _ => Output.notFound
      }
    case _ =>
      Output.notFound
  }

  def post(input: Input): Output = ujson.read(input.body)(id).strOpt match {
    case Some(id) =>
      store.get(id) match {
        case Some(_) => Output.conflict(s"Book with ID[$id] already exists.")
        case _ =>
          store.store(id, input.body)
          Output.ok(input.body)
      }
    case _ =>
      Output.notFound
  }

  def put(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.store(id, input.body)
      Output.ok(input.body)
    case _ =>
      Output.badRequest
  }

  def delete(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.delete(id) match {
        case Some(d) => Output.ok(d)
        case _ => Output.notFound
      }
    case _ =>
      Output.notFound
  }
}