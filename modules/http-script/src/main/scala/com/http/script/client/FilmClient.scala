package com.http.script.client

import com.http.script.{JsonResponse, applicationJson, baseUrl}

case class Film(id: String, name: String)

object FilmClient {

  val url: String => String = baseUrl(7030)

  def deleteAll: JsonResponse = JsonResponse(requests.delete(url("/films")))

  def getAll: JsonResponse = JsonResponse(requests.get(url("/films")))

  def get(id: String): JsonResponse = JsonResponse(requests.get(url(s"/film/$id")))

  def post(film: Film): JsonResponse = JsonResponse(requests.post(url("/film"), headers = applicationJson,
    data = ujson.Obj("id" -> film.id, "name" -> film.name)))

  def put(film: Film): JsonResponse = JsonResponse(requests.put(url(s"/film/${film.id}"), headers = applicationJson,
    data = ujson.Obj("id" -> film.id, "name" -> film.name)))

  def delete(id: String): JsonResponse = JsonResponse(requests.delete(url(s"/film/$id")))
}
