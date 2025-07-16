package com.http.stub.mapping.template

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, containing}
import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.matching.UrlPattern
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory

trait Posts extends Base {

  private val logger = LoggerFactory.getLogger(getClass)

  def post(uri: UrlPattern, status: Int = HttpStatus.OK_200, responseFileLocation: Option[String] = None): MappingBuilder = {
    logger.info(s"POST $uri => $status")
    val response = responseFileLocation match {
      case Some(location) => aResponse().withStatus(status).withBodyFile(location)
      case None => aResponse().withStatus(status).withBody("")
    }
    WireMock.post(uri).willReturn(response)
  }

  def postMatchingOnBody(uri: UrlPattern, status: Int = HttpStatus.OK_200, requestBodyMatch: String, responseBody: String): MappingBuilder = {
    logger.info(s"POST $uri => $requestBodyMatch => $responseBody => $status")
    WireMock.post(uri).withRequestBody(containing(requestBodyMatch))
      .willReturn(aResponse().withStatus(status).withBody(responseBody))
  }
}
