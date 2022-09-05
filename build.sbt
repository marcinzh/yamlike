ThisBuild / organization := "io.github.marcinzh"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.2.0"

ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
ThisBuild / watchForceTriggerOnAnyChange := true

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
)

val Deps = {
  object deps {
  }
  deps
}

lazy val root = project
  .in(file("."))
  .settings(sourcesInBase := false)
  .aggregate(yamlike, examples)

lazy val yamlike = project
  .in(file("modules/yamlike"))
  .settings(name := "yamlike")

lazy val examples = project
  .in(file("modules/examples"))
  .settings(name := "examples")
  .settings(scalaVersion := "3.2.1-RC1-bin-20220905-c93098b-NIGHTLY")
  .settings(scalacOptions += "-language:experimental.fewerBraces")
  .dependsOn(yamlike)
