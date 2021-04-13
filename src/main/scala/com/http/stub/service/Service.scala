package com.http.stub.service

import com.github.tomakehurst.wiremock.client.WireMock

trait Service {

  def stubWith(wireMock: WireMock): Unit
}
