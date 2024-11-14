package com.http.stub.mapping

import com.http.stub.dynamic.model._
import com.http.stub.dynamic.{Endpoint, TypedInMemoryStore}
import com.http.stub.mapping.endpoint.{Film, FilmEndpoint}

object FilmRoutes {

  val endpoint = new FilmEndpoint

  val all: Map[EndpointDefinition, Endpoint] = Map[EndpointDefinition, Endpoint](
    Get("/films") -> endpoint.listAll,
    Get("/film/*") -> endpoint.get,
    Post("/films/delete-all") -> endpoint.deleteAll,
    Post("/film") -> endpoint.post,
    Delete("/film/*") -> endpoint.delete,
  )
}
