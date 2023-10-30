package com.http.stub

import com.http.stub.dynamic.Router
import com.http.stub.mapping.{BookRoutes, FilmRoutes, GetMappings, PostMappings}
import com.http.stub.model.{DynaMockService, MockService, WireMockService}

object Service {

  val all: Seq[MockService] = Seq(
    WireMockService(port = 7010, name = "Get", mappings = GetMappings.all),
    WireMockService(port = 7020, name = "Post", mappings = PostMappings.all),
    DynaMockService(port = 7030, name = "Film", router = Router(FilmRoutes.all)),
    DynaMockService(port = 7040, name = "Book", router = Router(BookRoutes.all))
  )
}
