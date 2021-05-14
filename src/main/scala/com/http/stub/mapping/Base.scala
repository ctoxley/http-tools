package com.http.stub.mapping

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock._
import org.apache.http.HttpHeaders._
import org.slf4j.LoggerFactory

trait Base {

  object MediaType {
    val json = "application/json"
  }

  val logger = LoggerFactory.getLogger(getClass)

  lazy val firstPathParam: String = pathParam(0)
  lazy val secondPathParam: String = pathParam(1)
  lazy val thirdPathParam: String = pathParam(2)
  lazy val fourthPathParam: String = pathParam(3)
  lazy val fifthPathParam: String = pathParam(4)
  def pathParam(index: Int): String = s"{{request.path.[$index]}}"

  def firstQueryParam(key: String): String = queryParam(key, 0)
  def secondQueryParam(key: String): String = queryParam(key,1)
  def thirdQueryParam(key: String): String = queryParam(key,2)
  def fourthQueryParam(key: String): String = queryParam(key,3)
  def fifthQueryParam(key: String): String = queryParam(key,4)
  def queryParam(key: String, index: Int): String = s"{{request.query.$key.$index}}"
  def queryParam(key: String): String = s"{{request.query.$key}}"

  def aJsonResponse(status: Int): ResponseDefinitionBuilder =
    aResponse.withStatus(status)
    .withHeader(CONTENT_TYPE, MediaType.json)
    .withTransformers("response-template")
}
