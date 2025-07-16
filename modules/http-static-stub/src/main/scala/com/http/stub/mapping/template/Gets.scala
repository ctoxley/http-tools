package com.http.stub.mapping.template

import com.github.tomakehurst.wiremock.client.{MappingBuilder, ResponseDefinitionBuilder, WireMock}
import com.github.tomakehurst.wiremock.matching.UrlPattern
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

trait Gets extends Base {

  private val logger = LoggerFactory.getLogger(getClass)

  def get(uri: UrlPattern, status: Int = HttpStatus.OK_200, responseBody: String = "{}",
          additionalRequestMatching: MappingBuilder => MappingBuilder = mb => mb,
          additionalResponseMapping: ResponseDefinitionBuilder => ResponseDefinitionBuilder = rdb => rdb): MappingBuilder = {

    logger.info(s"GET $uri => $status => $responseBody")
    additionalRequestMatching(WireMock.get(uri))
      .willReturn(additionalResponseMapping(aJsonResponse(status).withBody(responseBody)))
  }

  def getFile(uri: UrlPattern, status: Int = HttpStatus.OK_200, responseFileLocation: String,
              additionalRequestMatching: MappingBuilder => MappingBuilder = mb => mb): MappingBuilder = {
    logger.info(s"GET $uri => $status => $responseFileLocation")
    additionalRequestMatching(WireMock.get(uri))
      .willReturn(aJsonResponse(status).withBodyFile(responseFileLocation))
  }
}
