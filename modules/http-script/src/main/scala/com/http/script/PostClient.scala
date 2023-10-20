package com.http.script

object PostClient {

  val baseUri = "http://localhost:7020/post"

  def postToDevNull = JsonResponse(requests.post(s"$baseUri/dev/null"))

  def postMatchingBody =
    JsonResponse(requests.post(s"$baseUri/body/match/true", data = ujson.Obj("match" -> "true")))
}
