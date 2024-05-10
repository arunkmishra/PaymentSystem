import Dependencies._
import MyUtil._

ThisBuild / organization := "com.github.psp"
ThisBuild / scalaVersion := "2.13.13"
ThisBuild / scalacOptions := Seq("-Wunused:imports")

lazy val `paymentsystem` =
  project
    .in(file("."))
    .settings(name := "PaymentSystem")
    .settings(dependencies)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    com.typesafe.akka.`akka-actor-typed`,
    com.typesafe.akka.`akka-http`,
    com.typesafe.`scala-logging`.`scala-logging`,
    ch.qos.logback.`logback-classic`,
  )
)
