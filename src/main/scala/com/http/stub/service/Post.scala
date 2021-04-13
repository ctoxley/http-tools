package com.http.stub.service
import com.github.tomakehurst.wiremock.client.{MappingBuilder, WireMock}
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport
import com.http.stub.WireMockCommonMapping.post

class Post extends Service {

  private def devNullMapping: MappingBuilder = post(uri = "/dev/null")

  override def allMappings = Seq(devNullMapping)
}
