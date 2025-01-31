package com.http

package object script {

  val schemeAndBase: String = "http://localhost"

  val applicationJson: Map[String, String] = Map("Content-Type" -> "application/json")

  def baseUrl(port: Int): String => String = (path: String) => s"$schemeAndBase:$port$path"
}
