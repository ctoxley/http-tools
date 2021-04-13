package com.http.stub.service

import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport

trait Service {

  def allMappings: Seq[MappingBuilder]

  def stubWith(wireMock: WireMock): Unit = {
    allMappings.foreach(m => wireMock.importStubMappings(stubImport().stub(m)))
  }
}
