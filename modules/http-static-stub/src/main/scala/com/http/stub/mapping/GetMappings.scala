package com.http.stub.mapping

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.{equalTo, urlEqualTo, urlMatching, urlPathEqualTo}
import com.http.stub.mapping.template.Gets

object GetMappings extends Gets {

  val all: Seq[MappingBuilder] = Seq(
    getFile(
      uri = urlEqualTo("/get/file"),
      responseFileLocation = "get/file/get-file.json"
    ),
    get(
      uri = urlEqualTo("/get/empty")
    ),
    get(
      uri = urlEqualTo("/get/fixed-delay"),
      responseBody = s"""{"delay":"10 seconds"}""",
      additionalResponseMapping = _.withFixedDelay(10000)
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
      responseBody = s"""{"name":"${queryParam("name")}"}""",
      additionalRequestMatching = r => r.withQueryParam("name", equalTo("true"))
    )
  )
}
