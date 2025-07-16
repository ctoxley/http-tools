package com.http.script

import com.http.script.JsonResponse.ValueOpts
import requests.{RequestFailedException, Response}
import ujson.Value.Value

import scala.annotation.tailrec

case class JsonResponse(json: Value, statusCode: Int) {

  lazy val text: String = json.asText
  lazy val is2xx: Boolean = statusCode >= 200 && statusCode <= 299
  lazy val isOk: Boolean = statusCode == 200
  lazy val isCreated: Boolean = statusCode == 201
  lazy val isNoContent: Boolean = statusCode == 204
  lazy val isBadRequest: Boolean = statusCode == 400
  lazy val isNotFound: Boolean = statusCode == 404
  lazy val isConflicted: Boolean = statusCode == 409

  def navigateToValue(field: String, fields: String*): Value = {
    @tailrec
    def it(json: Value, fields: Seq[String]): Value =
      fields match {
        case Seq(head) if json.isArr && head.toIntOption.isDefined => json.at(head.toInt)
        case Seq(head) if json.obj.contains(head) => json(head)
        case Seq(head, tail@_*) if json.isArr && head.toIntOption.isDefined => it(json.at(head.toInt), tail)
        case Seq(head, tail@_*) if json.obj.contains(head) => it(json(head), tail)
        case _ => ujson.Null
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

  private def read(response: Response): Value =
    if (response.contentLength.contains(0))
      ujson.Null
    else
      ujson.read(response)

  implicit final class ValueOpts(private val json: Value) extends AnyVal {

    def isArr: Boolean = json match {
      case ujson.Arr(_) => true
      case _ => false
    }

    def asText: String = json match {
      case ujson.Null => ""
      case ujson.True => "true"
      case ujson.False => "false"
      case ujson.Str(s) => s
      case ujson.Num(n) => if (n.isValidInt) n.toInt.toString else n.toString
      case a: ujson.Arr => ujson.write(a)
      case o: ujson.Obj => ujson.write(o)
    }

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

    def firstValueInObj(field: String, value: Value): Value = json match {
      case ujson.Arr(items) => items.collectFirst {
        case o @ ujson.Obj(subItems) if subItems.contains(field) && subItems(field) == value => o
      }.getOrElse(ujson.Null)
      case ujson.Obj(items) =>
        items.collectFirst {
          case fv @ (f, v) if f == field && v == value => fv
        }.map(_._2).getOrElse(ujson.Null)
      case _ => ujson.Null
    }

    def firstValueInObj(field: String): Value = json match {
      case ujson.Arr(items) => items.collectFirst {
        case o @ ujson.Obj(subItems) if subItems.contains(field) => o
      }.getOrElse(ujson.Null)
      case ujson.Obj(items) =>
        items.collectFirst {
          case fv @ (f, _) if f == field => fv
        }.map(_._2).getOrElse(ujson.Null)
      case _ => ujson.Null
    }

    def head: Value = json match {
      case ujson.Arr(items) if items.nonEmpty => items.head
      case ujson.Obj(items) if items.nonEmpty => items.head._2
      case _ => ujson.Null
    }

    def at(index: Int): Value = json match {
      case ujson.Arr(items) if items.size >= index => items(index)
      case _ => ujson.Null
    }

    def length: Int = json match {
      case ujson.Arr(items) => items.length
      case ujson.Obj(items) => items.size
      case _ => 0
    }
  }
}