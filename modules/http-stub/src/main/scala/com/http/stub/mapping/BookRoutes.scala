package com.http.stub.mapping

import com.http.stub.dynamic.model.{Delete, EndpointDefinition, Get, Put}
import com.http.stub.dynamic.{Endpoint, Input, Output}
import com.http.stub.mapping.endpoint.BookEndpoint

object BookRoutes {

  val endpoint = new BookEndpoint

  val all: Map[EndpointDefinition, Endpoint] = Map[EndpointDefinition, Endpoint](
    Get("/book/*") -> endpoint.get,
    Put("/book/*") -> endpoint.put,
    Delete("/book/*") -> endpoint.delete,
  )
}
