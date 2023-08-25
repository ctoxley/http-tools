
lazy val wiremockWrapper = (project in file("modules/http-stub"))
  .settings(
      name := "http-stub",
      scalaVersion := "2.13.4",
      libraryDependencies ++= Seq(
          "com.github.tomakehurst" % "wiremock" % "3.0.0-beta-10",
          "org.slf4j" % "slf4j-simple" % "2.0.7"
      )
  )
// .settings(resolvers += Resolver.jcenterRepo)