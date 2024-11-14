package com.http.script.client

import com.http.script.{JsonResponse, SchemeAndBase}

case class Book(id: String, title: String)

object BookClient {

  val BaseUri = s"$SchemeAndBase:7040"

  def deleteAll: JsonResponse = JsonResponse(requests.post("/books/delete-all"))

  def get(id: String): JsonResponse = JsonResponse(requests.get(s"$BaseUri/book/$id"))

  def post(book: Book) = JsonResponse(requests.post(s"$BaseUri/book", headers = Map("Content-Type" -> "application/json"),
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def put(book: Book) = JsonResponse(requests.put(s"$BaseUri/book/${book.id}", headers = Map("Content-Type" -> "application/json"),
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def delete(id: String): JsonResponse = JsonResponse(requests.delete(s"$BaseUri/book/$id"))
}
