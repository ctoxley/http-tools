package com.http.stub.service

import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.http.stub.PostMapping.{post, postMatchingOnBody}

class Post extends Service {

  override def allMappings = Seq(
    post(
      uri = urlEqualTo("/dev/null")
    ),
    postMatchingOnBody(
      uri = urlEqualTo("/post/body/match/true"),
      requestBodyMatch = "true",
      responseBody = """{"matched":true}"""
    )
  )
}
