
addCommandAlias("runHttpStub", "; httpStub/run")

lazy val httpScript = (project in file("modules/http-script"))
  .settings(
    name := "http-stub",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.8.0",
      "com.lihaoyi" %% "upickle" % "3.1.2",
      "com.lihaoyi" %% "os-lib" % "0.9.1",
      "com.lihaoyi" %% "utest" % "0.8.5" % "test"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

lazy val httpStub = (project in file("modules/http-stub"))
  .settings(
      name := "http-stub",
      scalaVersion := "2.13.4",
      libraryDependencies ++= Seq(
          "com.github.tomakehurst" % "wiremock" % "3.0.0-beta-10",
          "com.lihaoyi" %% "upickle" % "3.1.2",
          "ch.qos.logback" % "logback-classic" % "1.4.7",
          "ch.qos.logback" % "logback-core" % "1.4.7",
          "org.slf4j" % "slf4j-api" % "2.0.5",
          "com.lihaoyi" %% "utest" % "0.8.5" % "test"
      )
  )