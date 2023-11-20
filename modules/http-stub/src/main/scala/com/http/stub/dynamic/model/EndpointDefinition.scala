package com.http.stub.dynamic.model

import com.http.stub.dynamic.model.Headers.isJsonContentType
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod._
import org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON

import java.util.UUID

sealed trait EndpointDefinition {

  val path: String
  val pathParts: PathParts = PathParts.from(path)

  def method: String = this match {
    case _: Get => Http.Get
    case _: Post => Http.Post
    case _: Put => Http.Put
    case _: Delete => Http.Delete
  }

  def supports(method: String, contentType: Option[String]): Boolean =
    method.equalsIgnoreCase(this.method) && onlyCheckContentTypeForPostAndPut(contentType)

  private def onlyCheckContentTypeForPostAndPut(contentType: Option[String]) =
    if (method.equalsIgnoreCase(Http.Post) || method.equalsIgnoreCase(Http.Put))
      Headers.isJsonContentType(contentType) else true
}

@upickle.implicits.key("GET") case class Get(path: String) extends EndpointDefinition
@upickle.implicits.key("POST") case class Post(path: String) extends EndpointDefinition
@upickle.implicits.key("PUT") case class Put(path: String) extends EndpointDefinition
@upickle.implicits.key("DELETE") case class Delete(path: String) extends EndpointDefinition

case class Headers(headers: Map[String, String]) {

  val contentType = headers.get(Http.ContentType)
  val hasContentTypeJson = isJsonContentType(contentType)
  val requestId = headers.get("X-Request-ID").getOrElse(UUID.randomUUID().toString)
}

object Headers {

  val Empty = Headers(Map.empty)

  def isJsonContentType(contentType: Option[String]) = contentType.filter(_.startsWith(Http.Json)).isDefined
}

object Http {

  val Get = GET.name()
  val Post = POST.name()
  val Put = PUT.name()
  val Delete = DELETE.name()
  val Patch = PATCH.name()

  val Json = APPLICATION_JSON.getContentTypeField.getValue
  val ContentType = HttpHeader.CONTENT_TYPE.asString()
}