package com.http.stub.mapping

import com.http.stub.dynamic.model.{EndpointDefinition, Get}
import com.http.stub.dynamic.{Endpoint, Input, Output}

object BookRoutes {

  val all: Map[EndpointDefinition, Endpoint] = Map[EndpointDefinition, Endpoint](
    Get("/book") -> ((_: Input) => Output.ok("book-hello")),
  )
}
