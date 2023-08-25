# http-tools

## http-stub

[WireMock](https://wiremock.org/) is a great tool to stub endpoints.

This module wraps WireMock and provides a template for stubbing endpoints.

#### Configuration

  - `.stubCorsEnabled(true)` Automatic sending of CORS headers on stub responses.
  - `.notifier(new Slf4jNotifier(true))` Use SLF4 for logging. (WireMock wraps all logging in its own `Notifier` interface)
  - `.extensions(new ResponseTemplateTransformer(false))` Enable attributes of the request to be used in generating the response. 

