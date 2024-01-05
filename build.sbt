ThisBuild / organization := "io.github.marcinzh"
ThisBuild / version := "0.2.0"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
)

val Deps = {
  object deps {
    val zio = "dev.zio" %% "zio" % "1.0.18"
  }
  deps
}

lazy val root = project
  .in(file("."))
  .settings(name := "yamlike-root")
  .settings(sourcesInBase := false)
  .settings(dontPublishMe: _*)
  .aggregate(
    yamlist,
    yamlist_examples,
    yamlayer,
  )

lazy val yamlist = project
  .in(file("modules/yamlist"))
  .settings(name := "yamlist")

lazy val yamlist_examples = project
  .in(file("modules/yamlist-examples"))
  .settings(name := "yamlist-examples")
  .settings(dontPublishMe: _*)
  .dependsOn(yamlist)

lazy val yamlayer = project
  .in(file("modules/yamlayer"))
  .settings(name := "yamlayer")
  .settings(crossScalaVersions := Seq(scalaVersion.value, "2.13.12"))
  .settings(libraryDependencies += Deps.zio)

//=================================================

lazy val dontPublishMe = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
ThisBuild / watchForceTriggerOnAnyChange := true

//=================================================

ThisBuild / description := "YAML-like list syntax for Scala 3"
ThisBuild / organizationName := "marcinzh"
ThisBuild / homepage := Some(url("https://github.com/marcinzh/yamlike"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/marcinzh/yamlike"), "scm:git@github.com:marcinzh/yamlike.git"))
ThisBuild / licenses := List("MIT" -> new URL("http://www.opensource.org/licenses/MIT"))
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  isSnapshot.value match {
    case true => Some("snapshots" at nexus + "content/repositories/snapshots")
    case false => Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}
ThisBuild / pomExtra := (
  <developers>
    <developer>
      <id>marcinzh</id>
      <name>Marcin Żebrowski</name>
      <url>https://github.com/marcinzh</url>
    </developer>
  </developers>
)
