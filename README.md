# http-tools

## http-stub

[WireMock](https://wiremock.org/) is a great tool to stub endpoints.

This module wraps WireMock and provides a template for stubbing endpoints.

To run use both static and dynamic stubs:

```sbt runDynamicStub```
```sbt runStaticStub```

#### Configuration

  - `.stubCorsEnabled(true)` Automatic sending of CORS headers on stub responses.
  - `.notifier(new Slf4jNotifier(true))` Use SLF4 for logging. (WireMock wraps all logging in its own `Notifier` interface)
  - `.extensions(new ResponseTemplateTransformer(false))` Enable attributes of the request to be used in generating the response. 

### Dynamic stub

For a stub that can cache data please see `http-dynamic-stub`.

## http-script

This module is a base for creating scripts to call http endpoints.

To run the tests you need `http-dynamic-stub` and `http-static-stub` running. 