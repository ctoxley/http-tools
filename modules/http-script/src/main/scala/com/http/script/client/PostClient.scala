package com.http.script.client

import com.http.script.{JsonResponse, schemeAndHost}

object PostClient {

  private val baseUri = s"$schemeAndHost:7020/post"

  def postToDevNull: JsonResponse = JsonResponse(requests.post(s"$baseUri/dev/null"))

  def postMatchingBody: JsonResponse =
    JsonResponse(requests.post(s"$baseUri/body/match/true", data = ujson.Obj("match" -> "true")))
}
