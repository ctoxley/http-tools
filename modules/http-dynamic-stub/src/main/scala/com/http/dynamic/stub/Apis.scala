package com.http.dynamic.stub

import com.http.dynamic.stub.route.BookRoutes

object Apis {

  val all: Seq[Api] = Seq(
    Api(port = 7030, name = "Book", router = Router(BookRoutes.all()))
  )
}
