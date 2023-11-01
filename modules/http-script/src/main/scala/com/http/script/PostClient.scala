package com.http.script

object PostClient {

  val BaseUri = s"$SchemeAndBase:7020/post"

  def postToDevNull = JsonResponse(requests.post(s"$BaseUri/dev/null"))

  def postMatchingBody =
    JsonResponse(requests.post(s"$BaseUri/body/match/true", data = ujson.Obj("match" -> "true")))
}
