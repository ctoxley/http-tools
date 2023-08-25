package com.http.stub

import com.github.tomakehurst.wiremock.common.Slf4jNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer

object Configuration {

  val wiremock = WireMockConfiguration.options
    .usingFilesUnderDirectory("modules/http-stub/src/main/resources")
    .stubCorsEnabled(true)
    .notifier(new Slf4jNotifier(true))
    .extensions(new ResponseTemplateTransformer(false))
}
