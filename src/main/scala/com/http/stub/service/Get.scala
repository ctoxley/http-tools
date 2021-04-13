package com.http.stub.service
import com.http.stub.WireMockCommonMapping.{get, getFile}

class Get extends Service {

  val getFileMapping = getFile(uri = "/get/file", fileLocation = "get/file/get-file.json")
  val getEmpty = get(uri = "/get/empty", body = "{}")

  override def allMappings = Seq(getFileMapping, getEmpty)
}
