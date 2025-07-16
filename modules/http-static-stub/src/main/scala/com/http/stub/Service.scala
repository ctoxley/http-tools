package com.http.stub

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.http.stub.mapping.{GetMappings, PostMappings}

case class Service(name: String, host: String = "localhost", port: Int, mappings: Seq[MappingBuilder])

object Service {

  val all: Seq[Service] = Seq(
    Service(port = 7010, name = "Get", mappings = GetMappings.all),
    Service(port = 7020, name = "Post", mappings = PostMappings.all),
  )
}
