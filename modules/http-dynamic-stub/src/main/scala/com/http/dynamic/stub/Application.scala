package com.http.dynamic.stub

import com.http.dynamic.stub.server.StubServer
import org.slf4j.LoggerFactory

object Application {

  private val logger = LoggerFactory.getLogger(getClass)
  
  @main def start(): Unit = {
      val stubServer = new StubServer()
      Apis.all.foreach { service =>
        logger.info("Starting dynamic stub API {} on {}:{}", service.name, service.host, service.port)
        stubServer.importRoutes(service)
      }
      stubServer.start()
    }
    
}
