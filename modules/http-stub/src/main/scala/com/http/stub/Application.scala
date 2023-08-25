package com.http.stub

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport
import com.http.stub.service.Service
import org.slf4j.LoggerFactory

object Application extends App {

  private val logger = LoggerFactory.getLogger(getClass)

  private def startServer(services: Seq[Service]) =
    services.foreach(s => {
      logger.info("Starting {} on {}:{}", s.name, s.host, s.port)
      val server = new WireMockServer(Configuration.wiremock.port(s.port))
      server.start()

      val wireMock = new WireMock(s.host, server.port)
      s.mappings.foreach(m => wireMock.importStubMappings(stubImport().stub(m)))
    })

  startServer(Services.all)
}
