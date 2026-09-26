val tapirVersion = "1.13.19"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "novojishoapi",
    version := "1.0.0",
    organization := "com.aranadedoros.novojishoapi",
    scalaVersion := "3.9.0",
    javacOptions ++= Seq("--release", "21"),
    scalacOptions ++= Seq(
      "-java-output-version", "21",
      "-deprecation",
      "-feature",
      "-unchecked"
    ),
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-jsoniter-scala" % tapirVersion,
      "com.softwaremill.sttp.client4" %% "core" % "4.0.26",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.40.1",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.40.1",
      "ch.qos.logback" % "logback-classic" % "1.6.3",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub4-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.20" % Test,
      "com.softwaremill.sttp.client4" %% "jsoniter" % "4.0.26" % Test,
      "com.lihaoyi" %% "ujson" % "4.4.3"
    )
  )
)
