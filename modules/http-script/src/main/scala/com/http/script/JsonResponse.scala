package com.http.script

import requests.Response
import ujson.Value.Value

import scala.annotation.tailrec

sealed trait ValidationResult
case object Success extends ValidationResult
case class Failure(message: String) extends ValidationResult

case class JsonResponse(json: Value, statusCode: Int) {

  lazy val text = ujson.write(json)

  lazy val statusOfSuccess = statusCode >= 200 && statusCode <= 299
  lazy val statusOk = statusCode == 200
  lazy val statusNoContent = statusCode == 204
  lazy val statusBadRequest = statusCode == 400
  lazy val statusNotFound = statusCode == 404
  lazy val statusConflicted = statusCode == 409

  def apply(field: String): Value = json(field)

  def fieldExists(field: String): Boolean = json.obj.contains(field)

  def fieldOpt(field: String): Option[String] =
    if (fieldExists(field)) json(field).strOpt else None

  def navigateTo(path: String): Option[Value] = {
    @tailrec
    def it(path: String, json: Value): Option[Value] = path.split("\\.") match {
      case Array(head) => if (json.obj.contains(head)) Some(json(head)) else None
      case Array(head, tail@_*) => if(json.obj.contains(head)) it(tail.mkString("."), json(head)) else None
    }
    it(path, json)
  }
}

object JsonResponse {

  def apply(response: Response): JsonResponse = {
    val body = if (response.contentLength == Some(0)) ujson.Null else ujson.read(response)
    JsonResponse(body, response.statusCode)
  }

  def isEqualByPath(path: String, expected: String, obj: JsonResponse): Boolean =
    obj.navigateTo(path).filter(_.isNull == false).map(_.str).contains(expected)

  def isEqual(field: String, expected: String, obj: JsonResponse): Boolean =
    isEqualByPath(field, expected, obj)

  def isPresentAndNotNullByPath(path: String, jsonResponse: JsonResponse): Boolean =
    jsonResponse.navigateTo(path).filter(_.isNull == false).isDefined

  def isPresentAndNotNull(field: String, jsonResponse: JsonResponse): Boolean = {
    val fieldExists = jsonResponse.fieldExists(field)
    val fieldNotNull = fieldExists && jsonResponse(field).isNull == false
    fieldExists && fieldNotNull
  }
}

