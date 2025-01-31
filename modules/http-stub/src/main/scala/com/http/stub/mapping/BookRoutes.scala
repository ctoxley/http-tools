package com.http.stub.mapping

import com.http.stub.dynamic.Endpoint
import com.http.stub.dynamic.model.{Delete, EndpointDefinition, Get, Put, Post}
import com.http.stub.mapping.endpoint.JsonEndpoints

object BookRoutes {

  val endpoint = new JsonEndpoints("id")

  val all: Map[EndpointDefinition, Endpoint] = Map[EndpointDefinition, Endpoint](
    Get("/books") -> endpoint.listAll,
    Get("/book/*") -> endpoint.get,
    Delete("/books") -> endpoint.deleteAll,
    Post("/book") -> endpoint.post,
    Put("/book/*") -> endpoint.put,
    Delete("/book/*") -> endpoint.delete,
  )
}
