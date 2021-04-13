package com.http.stub.service
import com.http.stub.Mapping.{fifthPathParam, get, getFile, thirdPathParam}

class Get extends Service {

  private val getJsonFile =
    getFile(
      uri = "/get/file",
      responseFileLocation = "get/file/get-file.json"
    )
  private val getEmpty =
    get(
      uri = "/get/empty"
    )
  private val getTwoPathParams =
    get(
      uri = "/get/param1/(.*)/param2/(.*)",
      responseBody = s"""{"param1":"$thirdPathParam","param2":"$fifthPathParam"}"""
    )

  override def allMappings = Seq(getJsonFile, getEmpty, getTwoPathParams)
}
