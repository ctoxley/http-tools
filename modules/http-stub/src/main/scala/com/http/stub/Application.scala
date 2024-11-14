package com.http.stub

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport
import com.http.stub.dynamic.DynaMockServer
import com.http.stub.model.{Configuration, DynaMockService, MockService, WireMockService}
import org.slf4j.LoggerFactory

object Application extends App {

  private val logger = LoggerFactory.getLogger(getClass)

  private def startWireMockService(service: WireMockService) = {
    logger.info("Starting WireMock service {} on {}:{}", service.name, service.host, service.port)
    val server = new WireMockServer(Configuration.wiremock.port(service.port))
    server.start()

    val wireMock = new WireMock(service.host, server.port)
    service.mappings.foreach(m => wireMock.importStubMappings(stubImport().stub(m)))
  }

  private def startServices(services: Seq[MockService]) = {
    services.collect { case s: WireMockService => s }.foreach(startWireMockService)
    val dynaMockServices = services.collect { case s: DynaMockService => s }
    if (dynaMockServices.nonEmpty) {
      val dynaMockServer = DynaMockServer()
      dynaMockServices.foreach { service =>
        logger.info("Starting DynaMock service {} on {}:{}", service.name, service.host, service.port)
        dynaMockServer.importRoutes(service)
      }
      dynaMockServer.start()
    }
  }

  startServices(Service.all)
}
