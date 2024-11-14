package com.http.script.client

import com.http.script.{JsonResponse, SchemeAndBase}

case class Film(id: String, name: String)

object FilmClient {

  val BaseUri = s"$SchemeAndBase:7030"

  def deleteAll: JsonResponse = JsonResponse(requests.post("/films/delete-all"))

  def get(id: String): JsonResponse = handleErrors(requests.get(s"$BaseUri/film/$id"))

  def post(film: Film) = JsonResponse(requests.post(s"$BaseUri/film", headers = Map("Content-Type" -> "application/json"),
    data = ujson.Obj("id" -> film.id, "name" -> film.name)))

  def delete(id: String): JsonResponse = JsonResponse(requests.delete(s"$BaseUri/film/$id"))
}
