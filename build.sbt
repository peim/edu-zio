val Http4sVersion  = "0.21.3"
val DoobieVersion = "0.8.8"
val TapirVersion   = "0.13.2"

lazy val root = (project in file("."))
  .settings(
    organization := "com.peim",
    name := "edu-zio",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.1",
    mainClass in Compile := Some("com.peim.Main"),
    libraryDependencies ++= Seq(
      "org.http4s"                  %% "http4s-blaze-server"      % Http4sVersion,
      "org.http4s"                  %% "http4s-blaze-client"      % Http4sVersion,
      "org.http4s"                  %% "http4s-circe"             % Http4sVersion,
      "org.http4s"                  %% "http4s-dsl"               % Http4sVersion,
      "io.circe"                    %% "circe-generic"            % "0.13.0",

      "dev.zio"                     %% "zio-streams"              % "1.0.0-RC18-2",
      "dev.zio"                     %% "zio-interop-cats"         % "2.0.0.0-RC12",
      "com.github.pureconfig"       %% "pureconfig"               % "0.12.3",

      "org.tpolecat"                %% "doobie-core"              % DoobieVersion,
      "org.tpolecat"                %% "doobie-h2"                % DoobieVersion,
      "org.tpolecat"                %% "doobie-hikari"            % DoobieVersion,

      "com.softwaremill.sttp.tapir" %% "tapir-core"               % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % TapirVersion,

      "org.specs2"                  %% "specs2-core"              % "4.9.3" % Test,
      "ch.qos.logback"              %  "logback-classic"          % "1.2.3"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
