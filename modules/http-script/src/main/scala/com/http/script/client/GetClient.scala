package com.http.script.client

import com.http.script.{JsonResponse, schemeAndHost}

object GetClient {

  private val baseUri = s"$schemeAndHost:7010/get"

  def getFile: JsonResponse = JsonResponse(requests.get(s"$baseUri/file"))

  def getEmpty: JsonResponse = JsonResponse(requests.get(s"$baseUri/empty"))

  def getWithTwoPathParams(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$baseUri/param1/$param1/param2/$param2"))

  def getWithQueryParamsListOfTwo(param1: String, param2: String): JsonResponse =
    JsonResponse(requests.get(s"$baseUri/query-param/list/two?list=$param1&list=$param2"))

  def getWithQueryParamMatchTrueOnly: JsonResponse =
    JsonResponse(requests.get(s"$baseUri/query-param/match/name/true?name=true"))

  def getPathParamAndQueryParam(pathParam: String, queryParam: String): JsonResponse =
    JsonResponse(requests.get(s"$baseUri/$pathParam?queryParam=$queryParam"))
}
