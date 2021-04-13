package com.http.stub.service
import com.http.stub.WireMockCommonMapping.{get, getFile}

class Get extends Service {

  private val getJsonFile = getFile(uri = "/get/file", responseFileLocation = "get/file/get-file.json")
  private val getEmpty = get(uri = "/get/empty")

  override def allMappings = Seq(getJsonFile, getEmpty)
}
