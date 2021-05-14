package com.http.stub.mapping

import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.matching.UrlPattern
import org.eclipse.jetty.http.HttpStatus

trait Gets extends Base {

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
