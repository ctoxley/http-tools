package com.http.stub

import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer

object Configuration {

  val wiremock = WireMockConfiguration.options
    .usingFilesUnderDirectory("src/main/resources")
    .stubCorsEnabled(true)
    .extensions(new ResponseTemplateTransformer(false))
}
