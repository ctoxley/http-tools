package com.http.dynamic.stub.route

import com.http.dynamic.stub.model.{EndpointInput, JsonInput, TextInput}
import com.http.dynamic.stub.store.SimpleInMemoryStore
import com.http.dynamic.stub.ApiOutput
import com.http.dynamic.stub.ApiOutput.ErrorCode.{ResourceIdMissing, ResourceIdMismatch}
import ujson.Value

final case class SimpleJsonRoute(idFieldName: String) {

  private val badRequestIdPathParamMissing = ApiOutput.badRequest(s"ID path param is missing.", ResourceIdMissing)
  private val badRequestIdInBodyMissing = ApiOutput.badRequest(s"ID in body is missing.", ResourceIdMissing)
  private val badRequestAllIdsMissing = ApiOutput.badRequest(s"ID path param and ID in body is missing.", ResourceIdMissing)

  private val store = new SimpleInMemoryStore[String, String]

  def deleteAll(input: EndpointInput): ApiOutput = {
    store.reset()
    ApiOutput.okNoContent
  }

  def listAll(input: EndpointInput): ApiOutput =
    if (store.allValues.isEmpty) ApiOutput.ok("[]")
    else ApiOutput.ok(store.allValues.mkString("[", ",", "]"))

  def get(input: EndpointInput): ApiOutput = input.path.second match
    case Some(id) => store.get(id).map(ApiOutput.ok).getOrElse(ApiOutput.notFound())
    case _ => badRequestIdPathParamMissing

  def post(input: EndpointInput): ApiOutput = idOpt(input) match
    case Some(id) =>
      store.get(id) match {
        case Some(_) => ApiOutput.conflict(s"Resource with ID[$id] already exists.")
        case _ =>
          store.store(id, input.body)
          ApiOutput.created(input.body)
      }
    case _ => badRequestIdInBodyMissing

  def put(input: EndpointInput): ApiOutput = (input.path.second, idOpt(input)) match
    case (Some(pathParamId), Some(bodyId)) if pathParamId == bodyId =>
      store.store(pathParamId, input.body)
      ApiOutput.created(input.body)
    case (Some(pathParamId), Some(bodyId)) =>
      ApiOutput.badRequest(s"Path param ID[$pathParamId] does not match body ID[$bodyId].", ResourceIdMismatch)
    case (Some(pathParamId), None) => badRequestIdInBodyMissing
    case (None, Some(bodyId)) => badRequestIdPathParamMissing
    case (None, None) => badRequestAllIdsMissing

  def delete(input: EndpointInput): ApiOutput = input.path.second match
    case Some(id) => store.delete(id).map(ApiOutput.ok).getOrElse(ApiOutput.notFound())
    case _ => badRequestIdPathParamMissing

  private def idOpt(input: EndpointInput) = input match {
    case JsonInput(_, _, _, json) =>
      if (!json.isNull && json.obj.contains(idFieldName)) json(idFieldName).strOpt else None
    case _ => None
  }
}

