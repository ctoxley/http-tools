package com.http.script

import requests.Response
import ujson.Value.Value

case class JsonResponse(json: Value) {

  lazy val text = ujson.write(json)

  def apply(field: String): Value = json(field)

  def safeStrOpt(field: String): Option[String] = if (json.obj.contains(field))
    json(field).strOpt else None

  def getObjByPath(path: String): JsonResponse = path.split("\\.") match {
    case Array(head) => JsonResponse(json(head))
    case Array(head, tail@_*) => JsonResponse(json(head)).getObjByPath(tail.mkString("."))
  }

  def assert(field: String, expected: String): Boolean = {
    required(field)
    val areEqual = json(field).str == expected
    if(areEqual == false) {
      throw new AssertionError(s"Field[$field] contains unexpected value[${json(field).str}] as expected[$expected].")
    }
    areEqual
  }

  def required(field: String): Unit = {
    if (json.obj.contains(field) == false)
      throw new AssertionError(s"Required field[$field] not present in JSON[$text].")
    if (json(field).isNull)
      throw new AssertionError(s"Required field[$field] present in JSON[$text] but value null.")
  }
}

object JsonResponse {

  def apply(response: Response): JsonResponse = JsonResponse(ujson.read(response))
}

