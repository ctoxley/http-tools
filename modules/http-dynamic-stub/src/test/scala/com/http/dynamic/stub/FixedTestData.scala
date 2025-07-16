package com.http.dynamic.stub

import com.http.dynamic.stub.Http.{Header, Headers, MediaType}
import com.http.dynamic.stub.model.{EndpointInput, JsonInput, PathParts, TextInput}

trait FixedTestData {

  val applicationJsonHeaders: Headers = Headers(Map(Header.contentType -> MediaType.applicationJson))
  
  // API Input
  val postApiInput: ApiInput = anApiInput().copy(method = "POST", PathParts.from("/item"), headers = applicationJsonHeaders)
  val putApiInput: ApiInput = anApiInput().copy(method = "PUT", PathParts.from("/item/1"), headers = applicationJsonHeaders)
  val getApiInput: ApiInput = anApiInput().copy(method = "GET", PathParts.from("/item/1"))
  val deleteApiInput: ApiInput = anApiInput().copy(method = "DELETE", PathParts.from("/item/1"))
  
  // Endpoint Input
  val postJsonInput: JsonInput = aJsonInput().copy(method = "POST", PathParts.from("/item"))
  val putJsonInput: JsonInput = aJsonInput().copy(method = "PUT", PathParts.from("/item/1"))
  val getTextInput: TextInput = aTextInput().copy(method = "GET", PathParts.from("/item/1"))
  val deleteTextInput: TextInput = aTextInput().copy(method = "DELETE", PathParts.from("/item/1"))

  def anApiInput(id: Int = 0): ApiInput = ApiInput(
    method = s"method$id", path = PathParts.from(s"/path$id"), body = "", headers = Headers()
  )

  def aTextInput(id: Int = 0): TextInput = TextInput(
    method = s"method$id", path = PathParts.from(s"/path$id"), body = ""
  )

  def aJsonInput(id: Int = 0): JsonInput = JsonInput(
    method = s"method$id", path = PathParts.from(s"/path$id"), body = "", json = ujson.Null
  )
}
