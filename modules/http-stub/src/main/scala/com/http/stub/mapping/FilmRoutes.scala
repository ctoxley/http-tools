package com.http.stub.mapping

import com.http.stub.dynamic.model._
import com.http.stub.dynamic.{Endpoint, TypedInMemoryStore}
import com.http.stub.mapping.endpoint.{Film, FilmEndpoints}

object FilmRoutes {

  val endpoint = new FilmEndpoints(new TypedInMemoryStore[String, Film])

  val all: Map[EndpointDefinition, Endpoint] = Map[EndpointDefinition, Endpoint](
    Get("/films") -> endpoint.listAll,
    Get("/film/*") -> endpoint.get,
    Delete("/films") -> endpoint.deleteAll,
    Post("/film") -> endpoint.post,
    Put("/film/*") -> endpoint.put,
    Delete("/film/*") -> endpoint.delete,
  )
}
