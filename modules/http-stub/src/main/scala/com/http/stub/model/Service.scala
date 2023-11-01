package com.http.stub.model

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.http.stub.dynamic.{Router, Routes}

sealed trait MockService {
  val name: String
  val host: String
  val port: Int
}

case class WireMockService(name: String, host: String = "localhost", port: Int, mappings: Seq[MappingBuilder]) extends MockService

@upickle.implicits.key("DynaMockService") case class DynaMockService(name: String, host: String = "localhost", port: Int, router: Router) extends MockService

object Service {

  def asJson(service: Seq[DynaMockService]): String =
    service.map(asJson).mkString("[", ",", "]")

  def asJson(service: DynaMockService): String =
    s"""{"name":"${service.name}","port":"${service.port}","host":"${service.host}","routes":${asJson(service.router.routes)}}"""

  def asJson(routes: Routes): String =
    routes.keys.map(k =>
      s"""{"method":"${k.method}","path":"${k.path}"}"""
    ).mkString("[", ",", "]")
}