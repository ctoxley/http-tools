package com.http.dynamic.stub.route

import com.http.dynamic.stub.Http.{Delete, Get, HttpEndpoint, Post, Put}
import com.http.dynamic.stub.Route

object BookRoutes {

  def all(route: SimpleJsonRoute = SimpleJsonRoute("id")): Map[HttpEndpoint, Route] =
    Map[HttpEndpoint, Route](
      Get("/books") -> route.listAll,
      Get("/book/*") -> route.get,
      Delete("/books") -> route.deleteAll,
      Post("/book") -> route.post,
      Put("/book/*") -> route.put,
      Delete("/book/*") -> route.delete,
    )
}
