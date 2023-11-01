package com.http.script

object GetClient {

  val BaseUri = s"$SchemeAndBase:7010/get"

  def getFile: JsonResponse = JsonResponse(requests.get(s"$BaseUri/file"))

  def getEmpty: JsonResponse = JsonResponse(requests.get(s"$BaseUri/empty"))

  def getWithTwoPathParams(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$BaseUri/param1/$param1/param2/$param2"))

  def getWithQueryParamsListOfTwo(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$BaseUri/query-param/list/two?list=$param1&list=$param2"))

  def getWithQueryParamMatchTrueOnly: JsonResponse =
    JsonResponse(requests.get(s"$BaseUri/query-param/match/name/true?name=true"))
}
