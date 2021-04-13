package com.http.stub

import com.http.stub.service.{Get, Post, Service}

case class HostPortAndService(host: String = "localhost", port: Int, service: Service)

object HostPortAndService {

  val get =  HostPortAndService(port = 7010, service = new Get())
  val post = HostPortAndService(port = 7020, service = new Post())

  val all: Seq[HostPortAndService] = Seq(get, post)
}
