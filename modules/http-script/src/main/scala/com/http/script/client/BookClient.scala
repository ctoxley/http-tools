package com.http.script.client

import com.http.script.{JsonResponse, baseUrl}

case class Book(id: String, title: String)

object BookClient {

  val url = baseUrl(7040)

  def deleteAll: JsonResponse = JsonResponse(requests.delete(url("/books")))

  def getAll: JsonResponse = JsonResponse(requests.get(url("/books")))

  def get(id: String): JsonResponse = JsonResponse(requests.get(url(s"/book/$id")))

  def post(book: Book) = JsonResponse(requests.post(url("/book"), headers = Map("Content-Type" -> "application/json"),
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def put(book: Book) = JsonResponse(requests.put(url(s"/book/${book.id}"), headers = Map("Content-Type" -> "application/json"),
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def delete(id: String): JsonResponse = JsonResponse(requests.delete(url(s"/book/$id")))
}
