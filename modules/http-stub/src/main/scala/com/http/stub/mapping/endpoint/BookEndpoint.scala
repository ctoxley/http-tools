package com.http.stub.mapping.endpoint

import com.http.stub.dynamic.{Input, Output, SimpleInMemoryStore}

class BookEndpoint {

  val store = new SimpleInMemoryStore[String, String]

  def get(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.get(id) match {
        case Some(d) => Output.ok(d)
        case _ => Output.notFound
      }
    case _ =>
      Output.notFound
  }

  def put(input: Input): Output = input.path.second match {
    case Some(id) =>
      store.store(id, input.body)
      Output.ok(input.body)
    case _ =>
      Output.notFound
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