package com.http.stub

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport
import org.slf4j.LoggerFactory

object Application extends App {

  private val logger = LoggerFactory.getLogger(getClass)

  private val configuration: WireMockConfiguration = WireMockConfiguration.options
    .usingFilesUnderDirectory("modules/http-static-stub/src/main/resources")
    .stubCorsEnabled(true)
    .notifier(new Slf4jNotifier(true))
    .extensions(new ResponseTemplateTransformer(false))

  Service.all.foreach { service =>
    logger.info("Starting stub APIs {} on {}:{}", service.name, service.host, service.port)

    val server = new WireMockServer(configuration.port(service.port))
    server.start()

    val wireMock = new WireMock(service.host, server.port)
    service.mappings.foreach(m => wireMock.importStubMappings(stubImport().stub(m)))
  }
}
