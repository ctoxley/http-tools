package com.http.script.client

import com.http.script.{JsonResponse, SchemeAndBase}

object PostClient {

  val BaseUri = s"$SchemeAndBase:7020/post"

  def postToDevNull = JsonResponse(requests.post(s"$BaseUri/dev/null"))

  def postMatchingBody =
    JsonResponse(requests.post(s"$BaseUri/body/match/true", data = ujson.Obj("match" -> "true")))
}
