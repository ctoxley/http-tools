package com.http.script

object GetClient {

  val baseUri = "http://localhost:7010/get"

  def getFile: JsonResponse = JsonResponse(requests.get(s"$baseUri/file"))

  def getEmpty: JsonResponse = JsonResponse(requests.get(s"$baseUri/empty"))

  def getWithTwoPathParams(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$baseUri/param1/$param1/param2/$param2"))

  def getWithQueryParamsListOfTwo(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$baseUri/query-param/list/two?list=$param1&list=$param2"))

  def getWithQueryParamMatchTrueOnly: JsonResponse =
    JsonResponse(requests.get(s"$baseUri/query-param/match/name/true?name=true"))
}
