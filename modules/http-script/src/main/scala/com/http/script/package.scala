package com.http

package object script {

  val schemeAndHost: String = "http://127.0.0.1"

  val applicationJson: Map[String, String] = Map("Content-Type" -> "application/json")

  def schemeAndHostAnd(port: Int): String => String = (path: String) => s"$schemeAndHost:$port$path"
}
