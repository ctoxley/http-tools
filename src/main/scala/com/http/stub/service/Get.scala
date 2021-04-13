package com.http.stub.service
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.StubImport.stubImport
import com.http.stub.WireMockCommonMapping.getFile

class Get extends Service {

  def getFileMapping = getFile(uri = "/get/file", fileLocation = "get/file/get-file.json")

  override def stubWith(wireMock: WireMock): Unit = {
    wireMock.importStubMappings(stubImport().stub(getFileMapping))
  }
}
