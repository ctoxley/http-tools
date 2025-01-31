package com.http.script

import requests.{RequestFailedException, Response}
import ujson.Value.Value

import scala.annotation.tailrec

case class JsonResponse(json: Value, statusCode: Int) {

  lazy val text: String = ujson.write(json)

  lazy val is2xx: Boolean = statusCode >= 200 && statusCode <= 299
  lazy val isOk: Boolean = statusCode == 200
  lazy val isNoContent: Boolean = statusCode == 204
  lazy val isBadRequest: Boolean = statusCode == 400
  lazy val isNotFound: Boolean = statusCode == 404
  lazy val isConflicted: Boolean = statusCode == 409

  def navigateToValue(field: String, fields: String*): Value = {
    @tailrec
    def it(json: Value, fields: Seq[String]): Value = fields match {
      case Seq(head) => if (json.obj.contains(head)) json(head) else ujson.Null
      case Seq(head, tail@_*) => if (json.obj.contains(head)) it(json(head), tail) else ujson.Null
    }
    it(json, field +: fields)
  }
}

object JsonResponse {

  def apply(requester: => Response): JsonResponse = {
    try {
      val response = requester
      JsonResponse(read(response), response.statusCode)
    } catch {
      case e: RequestFailedException =>
        JsonResponse(read(e.response), e.response.statusCode)
    }
  }

  private def read(response: Response) =
    if (response.contentLength.contains(0))
      ujson.Null
    else
      ujson.read(response)

  implicit final class ValueOpts(private val json: Value) extends AnyVal {

    def isNotNull: Boolean = !json.isNull

    def isEmpty: Boolean = json match {
      case ujson.Null => true
      case ujson.True => false
      case ujson.False => false
      case ujson.Str(s) => s.isEmpty
      case ujson.Num(_) => false
      case ujson.Arr(items) => items.isEmpty
      case ujson.Obj(items) => items.isEmpty
    }

    def isNotEmpty: Boolean = !isEmpty

    def firstValue(field: String, value: Value): Value = json match {
      case ujson.Arr(items) => items.find {
        case ujson.Obj(subItems) => subItems.contains(field) && subItems(field) == value
      }.getOrElse(ujson.Null)
      case ujson.Obj(items) =>
        items.collectFirst {
          case fv @ (f, v) if f == field && v == value => fv
        }.map(_._2).getOrElse(ujson.Null)
      case _ => ujson.Null
    }

    def firstValue(field: String): Value = json match {
      case ujson.Arr(items) => items.find {
        case ujson.Obj(subItems) => subItems.contains(field)
      }.getOrElse(ujson.Null)
      case ujson.Obj(items) =>
        items.collectFirst {
          case fv @ (f, _) if f == field => fv
        }.map(_._2).getOrElse(ujson.Null)
      case _ => ujson.Null
    }

    def length: Int = json match {
      case ujson.Arr(items) => items.length
      case _ => 0
    }
  }
}