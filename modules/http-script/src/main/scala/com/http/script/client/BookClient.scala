package com.http.script.client

import com.http.script.{JsonResponse, applicationJson, schemeAndHostAnd}

case class Book(id: String, title: String)

object BookClient {

  private val baseUrl: String => String = schemeAndHostAnd(7030)

  def deleteAll(): JsonResponse = JsonResponse(requests.delete(baseUrl("/books")))

  def listAll(): JsonResponse = JsonResponse(requests.get(baseUrl("/books")))

  def get(id: String): JsonResponse = JsonResponse(requests.get(baseUrl(s"/book/$id")))

  def post(book: Book): JsonResponse = JsonResponse(requests.post(baseUrl("/book"), headers = applicationJson,
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def put(book: Book): JsonResponse = JsonResponse(requests.put(baseUrl(s"/book/${book.id}"), headers = applicationJson,
    data = ujson.Obj("id" -> book.id, "title" -> book.title)))

  def delete(id: String): JsonResponse = JsonResponse(requests.delete(baseUrl(s"/book/$id")))
}
