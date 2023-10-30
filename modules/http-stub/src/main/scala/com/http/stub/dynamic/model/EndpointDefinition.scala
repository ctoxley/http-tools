package com.http.stub.dynamic.model

import org.eclipse.jetty.http.HttpMethod.{DELETE, GET, POST, PUT}
import org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON

sealed trait EndpointDefinition {

  val path: String
  val pathParts: PathParts = PathParts.from(path)

  def method: String = this match {
    case _: Get => GET.name()
    case _: Post => POST.name()
    case _: Put => PUT.name()
    case _: Delete => DELETE.name()
  }

  def supports(method: String, contentType: String): Boolean = {
    method.equalsIgnoreCase(this.method) && onlyCheckContentTypeForPostAndPut(contentType)
  }

  private def onlyCheckContentTypeForPostAndPut(contentType: String) =
    if (method.equalsIgnoreCase(POST.name()) || method.equalsIgnoreCase(PUT.name()))
      contentType.equalsIgnoreCase(APPLICATION_JSON.getContentTypeField.getValue) else true
}

@upickle.implicits.key("GET") case class Get(path: String) extends EndpointDefinition
@upickle.implicits.key("POST") case class Post(path: String) extends EndpointDefinition
@upickle.implicits.key("PUT") case class Put(path: String) extends EndpointDefinition
@upickle.implicits.key("DELETE") case class Delete(path: String) extends EndpointDefinition