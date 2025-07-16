package com.http.dynamic.stub

import com.http.dynamic.stub.model.{InputProcessingResult, InvalidInput, JsonInput, TextInput}

import scala.util.Try

class InputValidator {

  def validate(input: ApiInput): InputProcessingResult = {
    if (input.headers.hasContentTypeJson)
      if (input.body.isEmpty) {
        InvalidInput(s"JSON missing")
      } else tryParseJson(input)
    else
      TextInput(input.method, input.path, input.body)
  }

  private def tryParseJson(input: ApiInput) =
    Try {
      ujson.read(input.body)
    }.toEither match {
      case Right(v) => JsonInput(input.method, input.path, input.body, v)
      case Left(t) =>
        val encodedMessage = t.getMessage.replaceAll("\"", "'")
        InvalidInput(s"JSON parse error: $encodedMessage")
    }
}
