package com.http.script

import requests.{RequestFailedException, Response}

// Good idea ?

package object client {

  def handleErrors(request: => Response): JsonResponse =
    try {
      JsonResponse(request)
    } catch {
      case e: RequestFailedException => JsonResponse(e.response)
      case _ => ???
    }
}
