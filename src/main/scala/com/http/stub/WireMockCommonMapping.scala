package com.http.stub

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.client.{MappingBuilder, ResponseDefinitionBuilder, WireMock}
import com.github.tomakehurst.wiremock.recording.ResponseDefinitionBodyMatcherDeserializer
import org.apache.http.HttpHeaders._
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

object WireMockCommonMapping {

  private val logger = LoggerFactory.getLogger(getClass)

  object MediaType {
    val json = "application/json"
  }

  def post(uri: String, status: Int = HttpStatus.OK_200): MappingBuilder = {
    logger.info(s"POST $uri => $status")
    WireMock.post(uri).willReturn(aResponse().withStatus(status))
  }

  def get(uri: String, status: Int = HttpStatus.OK_200, body: String): MappingBuilder = {
    logger.info(s"GET $uri => $status => $body")
    WireMock.get(uri).willReturn(aJsonResponse(status).withBody(body))
  }

  def getFile(uri: String, status: Int = HttpStatus.OK_200, fileLocation: String): MappingBuilder = {
    logger.info(s"GET $uri => $status => $fileLocation")
    WireMock.get(uri).willReturn(aJsonResponse(status).withBodyFile(fileLocation))
  }

  def aJsonResponse(status: Int): ResponseDefinitionBuilder = aResponse.withStatus(status).withHeader(CONTENT_TYPE, MediaType.json)
}
