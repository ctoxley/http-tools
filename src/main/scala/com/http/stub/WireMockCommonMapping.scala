package com.http.stub

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.client.{MappingBuilder, ResponseDefinitionBuilder, WireMock}
import org.apache.http.HttpHeaders._
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

object WireMockCommonMapping {

  private val logger = LoggerFactory.getLogger(getClass)

  object MediaType {
    val json = "application/json"
  }

  def post(uri: String, status: Int = HttpStatus.OK_200, responseBody: String = "{}"): MappingBuilder = {
    logger.info(s"POST $uri => $status")
    WireMock.post(urlMatching(uri)).willReturn(aResponse().withStatus(status).withBody(responseBody))
  }

  def postMatchingOnBody(uri: String, status: Int = HttpStatus.OK_200, requestBodyMatch: String, responseBody: String): MappingBuilder = {
    logger.info(s"POST $uri => $requestBodyMatch => $responseBody => $status")
    WireMock.post(urlMatching(uri)).withRequestBody(containing(requestBodyMatch))
      .willReturn(aResponse().withStatus(status).withBody(responseBody))
  }

  def get(uri: String, status: Int = HttpStatus.OK_200, responseBody: String = "{}"): MappingBuilder = {
    logger.info(s"GET $uri => $status => $responseBody")
    WireMock.get(urlMatching(uri)).willReturn(aJsonResponse(status).withBody(responseBody))
  }

  def getFile(uri: String, status: Int = HttpStatus.OK_200, responseFileLocation: String): MappingBuilder = {
    logger.info(s"GET $uri => $status => $responseFileLocation")
    WireMock.get(urlMatching(uri)).willReturn(aJsonResponse(status).withBodyFile(responseFileLocation))
  }

  def aJsonResponse(status: Int): ResponseDefinitionBuilder = aResponse.withStatus(status)
    .withHeader(CONTENT_TYPE, MediaType.json)
    .withTransformers("response-template")
}
