package com.http.stub

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.client.{MappingBuilder, ResponseDefinitionBuilder, WireMock}
import com.github.tomakehurst.wiremock.matching.UrlPattern
import org.apache.http.HttpHeaders._
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

object Mapping {

  object MediaType {
    val json = "application/json"
  }

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
