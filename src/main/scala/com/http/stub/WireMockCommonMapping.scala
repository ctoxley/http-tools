package com.http.stub

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
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

  def getFile(uri: String, status: Int = HttpStatus.OK_200, fileLocation: String): MappingBuilder = {
    logger.info(s"GET $uri => $status")
    WireMock.get(uri)
      .willReturn(aResponse.withStatus(status).withHeader(CONTENT_TYPE, MediaType.json).withBodyFile(fileLocation))
  }
}
