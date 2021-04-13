
val wiremock = "com.github.tomakehurst" % "wiremock" % "2.27.2"
val logging = "org.slf4j" % "slf4j-simple" % "2.0.0-alpha1"

lazy val root = Project("http-stub", file("."))
  .settings(
    version := "0.1.0-SNAPSHOT",
    organization := "com.http",
    organizationName := "http",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(wiremock, logging)
  ).settings(resolvers += Resolver.jcenterRepo)
