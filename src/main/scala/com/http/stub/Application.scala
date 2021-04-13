package com.http.stub

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.slf4j.LoggerFactory

object Application extends App {

  private val logger = LoggerFactory.getLogger(getClass)

  private def startServers(hostPortAndService: Seq[HostPortAndService]) = hostPortAndService.foreach(hps => {
    logger.info("Starting {} on {}:{}", hps.service.getClass.getSimpleName, hps.host, hps.port)
    val server = new WireMockServer(Configuration.wiremock.port(hps.port))
    server.start()
    hps.service.stubWith(new WireMock(hps.host, server.port))
  })

  startServers(HostPortAndService.all)
}
