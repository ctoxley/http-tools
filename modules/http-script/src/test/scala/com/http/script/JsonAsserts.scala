package com.http.script

object JsonAsserts {

   def assertOk(response: JsonResponse, expectedBody: String = ""): Unit =
     utest.assert(response.isOk, response.text == expectedBody)

   def assertConflicted(response: JsonResponse, expectedMessage: String): Unit = {
     val errorMessage = response.navigateToValue("error", "message").strOpt
     val errorCode = response.navigateToValue("error", "code").strOpt
     utest.assert(
       response.isConflicted,
       errorMessage.contains(expectedMessage),
       errorCode.contains("resource-already-exists")
     )
   }
}
