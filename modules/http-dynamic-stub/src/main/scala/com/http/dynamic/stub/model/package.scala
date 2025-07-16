package com.http.dynamic.stub

import ujson.Value

package object model {

  sealed trait InputProcessingResult
  sealed trait EndpointInput extends InputProcessingResult {
    val method: String
    val path: PathParts
    val body: String
  }
  final case class JsonInput(method: String, path: PathParts, body: String, json: Value.Value) extends EndpointInput
  final case class TextInput(method: String, path: PathParts, body: String) extends EndpointInput
  final case class InvalidInput(error: String) extends InputProcessingResult
}
