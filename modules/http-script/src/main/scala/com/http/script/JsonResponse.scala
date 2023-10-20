package com.http.script

import requests.Response
import ujson.Value.Value

import scala.annotation.tailrec

case class JsonResponse(response: Response) {

  lazy val json = ujson.read(response)
  lazy val text = ujson.write(json)

  def apply(field: String): Value = json(field)

  def isSuccess = response.statusCode >= 200 && response.statusCode <= 299

  def safeStrOpt(field: String): Option[String] = if (json.obj.contains(field))
    json(field).strOpt else None

  def getObjByPath(path: String): Value = {
    @tailrec
    def it(path: String, json: Value): Value = path.split("\\.") match {
      case Array(head) => json(head)
      case Array(head, tail@_*) => it(tail.mkString("."), json(head))
    }
    it(path, json)
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

