package com.http.script

import com.http.script.JsonResponse.ValueOpts
import utest._

object JsonResponseTest extends TestSuite {

  private val messageBody = "My first message."
  private val messageSecondLabel = "FirstAttempt"
  private val messagePostCode = "ABC123"
  private val jsonStr = {
    s"""
       |{
       |  "Messages": [
       |    {
       |      "Body": "$messageBody",
       |      "ReceiptHandle": "AQEBzbVv...fqNzFw==",
       |      "MD5OfBody": "1000f835...a35411fa",
       |      "MD5OfMessageAttributes": "9424c491...26bc3ae7",
       |      "MessageId": "d6790f8d-d575-4f01-bc51-40122EXAMPLE",
       |      "Attributes": {
       |        "ApproximateFirstReceiveTimestamp": "1442428276921",
       |        "SenderId": "AIDAIAZKMSNQ7TEXAMPLE",
       |        "ApproximateReceiveCount": "5",
       |        "SentTimestamp": "1442428276921"
       |      },
       |      "Labels": ["Important", "$messageSecondLabel"],
       |      "MessageAttributes": {
       |        "PostalCode": {
       |          "DataType": "String",
       |          "StringValue": "$messagePostCode"
       |        },
       |        "City": {
       |          "DataType": "String",
       |          "StringValue": "Any City"
       |        }
       |      }
       |    }
       |  ],
       |  "Total": 1
       |}
       |""".stripMargin
  }

  val tests: Tests = Tests {

    test("Response status codes") {
      JsonResponse(ujson.Null, 199).is2xx ==> false
      JsonResponse(ujson.Null, 200).is2xx ==> true
      JsonResponse(ujson.Null, 299).is2xx ==> true
      JsonResponse(ujson.Null, 300).is2xx ==> false
      JsonResponse(ujson.Null, 201).isCreated ==> true
      JsonResponse(ujson.Null, 204).isNoContent ==> true
      JsonResponse(ujson.Null, 400).isBadRequest ==> true
      JsonResponse(ujson.Null, 404).isNotFound ==> true
      JsonResponse(ujson.Null, 409).isConflicted ==> true
    }
    test("Navigate to") {
      val json: JsonResponse = JsonResponse.apply(ujson.read(jsonStr), 200)
      json.navigateToValue("Total").num ==> 1
      json.navigateToValue("Messages", "0", "Body").str ==> messageBody
      json.navigateToValue("Messages", "0", "Labels", "1").str ==> messageSecondLabel
      json.navigateToValue("Messages", "0", "MessageAttributes", "PostalCode", "StringValue").str ==> messagePostCode
      json.navigateToValue("No", "Where") ==> ujson.Null
    }
    test("Is array") {
      val allJsonValues = Seq(ujson.Null, ujson.False, ujson.True, ujson.Str("str"), ujson.Num(0),
        ujson.Arr(ujson.Str("arr")), ujson.Obj("name" -> ujson.Str("obj")))
      allJsonValues.filter(_.isInstanceOf[ujson.Arr]).forall(_.isArr) ==> true
      allJsonValues.filterNot(_.isInstanceOf[ujson.Arr]).forall(_.isArr) ==> false
    }
    test("Is empty") {
      ujson.Null.isEmpty ==> true
      ujson.False.isEmpty ==> false
      ujson.True.isEmpty ==> false
      ujson.Str("").isEmpty ==> true
      ujson.Str("str").isEmpty ==> false
      ujson.Num(0).isEmpty ==> false
      ujson.Arr().isEmpty ==> true
      ujson.Arr(ujson.Str("arr")).isEmpty ==> false
      ujson.Obj().isEmpty ==> true
      ujson.Obj("name" -> ujson.Str("obj")).isEmpty ==> false
    }
    test("Is not empty") {
      ujson.Null.isNotEmpty ==> false
      ujson.False.isNotEmpty ==> true
      ujson.True.isNotEmpty ==> true
      ujson.Str("").isNotEmpty ==> false
      ujson.Str("str").isNotEmpty ==> true
      ujson.Num(0).isNotEmpty ==> true
      ujson.Arr().isNotEmpty ==> false
      ujson.Arr(ujson.Str("arr")).isNotEmpty ==> true
      ujson.Obj().isNotEmpty ==> false
      ujson.Obj("name" -> ujson.Str("obj")).isNotEmpty ==> true
    }
    test("As text") {
      ujson.Null.asText ==> ""
      ujson.False.asText ==> "false"
      ujson.True.asText ==> "true"
      ujson.Str("str").asText ==> "str"
      ujson.Num(0).asText ==> "0"
      ujson.Num(0.1).asText ==> "0.1"
      ujson.Arr(ujson.Str("arr")).asText ==> """["arr"]"""
      ujson.Obj("name" -> ujson.Str("obj")).asText ==> """{"name":"obj"}"""
    }
    test("First value given name and value") {
      val json: JsonResponse = JsonResponse.apply(ujson.read(jsonStr), 200)
      json.navigateToValue("Messages", "0").firstValueInObj("Body", ujson.Str(messageBody)) ==> ujson.Str(messageBody)
    }
    test("First value given name") {
      val json: JsonResponse = JsonResponse.apply(ujson.read(jsonStr), 200)
      json.navigateToValue("Messages", "0").firstValueInObj("Body") ==> ujson.Str(messageBody)
    }
    test("Head") {
      val json: JsonResponse = JsonResponse.apply(ujson.read(jsonStr), 200)
      json.navigateToValue("Messages").head.head ==> ujson.Str(messageBody)
    }
    test("At") {
      ujson.Arr(ujson.Str("1"), ujson.Str("2"), ujson.Str("3")).at(1) ==> ujson.Str("2")
    }
    test("Length") {
      ujson.Arr(ujson.Str("1"), ujson.Str("2"), ujson.Str("3")).length ==> 3
      ujson.Obj("name1" -> ujson.Str("obj1"), "name2" -> ujson.Str("obj2")).length ==> 2
      ujson.Null.length ==> 0
    }
  }
}
