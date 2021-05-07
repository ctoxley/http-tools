package com.http.stub

import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.http.stub.Mapping.aJsonResponse
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

object GetMapping {

  private val logger = LoggerFactory.getLogger(getClass)

  def get(uri: UrlPattern, status: Int = HttpStatus.OK_200, responseBody: String = "{}",
          additionalRequestMatching: MappingBuilder => MappingBuilder = mb => mb): MappingBuilder = {

    logger.info(s"GET $uri => $status => $responseBody")
    additionalRequestMatching(WireMock.get(uri))
      .willReturn(aJsonResponse(status).withBody(responseBody))
  }

  def getFile(uri: UrlPattern, status: Int = HttpStatus.OK_200, responseFileLocation: String,
              additionalRequestMatching: MappingBuilder => MappingBuilder = mb => mb): MappingBuilder = {
    logger.info(s"GET $uri => $status => $responseFileLocation")
    additionalRequestMatching(WireMock.get(uri))
      .willReturn(aJsonResponse(status).withBodyFile(responseFileLocation))
  }
}
