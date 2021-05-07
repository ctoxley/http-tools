package com.http.stub.service
import com.github.tomakehurst.wiremock.client.WireMock.{equalTo, urlEqualTo, urlMatching, urlPathEqualTo}
import com.http.stub.GetMapping.{get, getFile}
import com.http.stub.Mapping._

class Get extends Service {

  override def allMappings = Seq(
    getFile(
      uri = urlEqualTo("/get/file"),
      responseFileLocation = "get/file/get-file.json"
    ),
    get(
      uri = urlEqualTo("/get/empty")
    ),
    get(
      uri = urlMatching("/get/param1/(.*)/param2/(.*)"),
      responseBody = s"""{"param1":"$thirdPathParam","param2":"$fifthPathParam"}"""
    ),
    get(
      uri = urlMatching("/get/query-param/list/two(.*)"),
      responseBody = s"""{"list":"${firstQueryParam("list")},${secondQueryParam("list")}"}"""
    ),
    get(
      uri = urlPathEqualTo("/get/query-param/match/name/true"),
      additionalRequestMatching = r => r.withQueryParam("name", equalTo("true")),
      responseBody = s"""{"name":"${queryParam("name")}"}"""
    )
  )
}
