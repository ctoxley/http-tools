package com.http

package object script {

  val schemeAndHost: String = "http://localhost"

  val applicationJson: Map[String, String] = Map("Content-Type" -> "application/json")

  def schemeAndHostAnd(port: Int): String => String = (path: String) => s"$schemeAndHost:$port$path"
}
