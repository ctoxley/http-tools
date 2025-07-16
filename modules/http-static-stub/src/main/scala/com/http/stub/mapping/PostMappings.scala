package com.http.stub.mapping

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.http.stub.mapping.template.Posts

object PostMappings extends Posts {

  val all: Seq[MappingBuilder] = Seq(
    post(
      uri = urlEqualTo("/post/dev/null"),
    ),
    postMatchingOnBody(
      uri = urlEqualTo("/post/body/match/true"),
      requestBodyMatch = "true",
      responseBody = """{"matched":true}"""
    )
  )
}
