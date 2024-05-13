import Dependencies._
import MyUtil._

ThisBuild / organization := "com.github.psp"
ThisBuild / scalaVersion := "2.13.13"
ThisBuild / scalacOptions := Seq("-Wunused:imports")
resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val `paymentsystem` =
  project
    .in(file("."))
    .settings(name := "PaymentSystem")
    .settings(dependencies)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    com.typesafe.akka.`akka-actor-typed`,
    com.typesafe.akka.`akka-stream`,
    com.typesafe.akka.`akka-http`,
    com.typesafe.akka.`akka-http-spray-json`,
    com.typesafe.`scala-logging`.`scala-logging`,
    ch.qos.logback.`logback-classic`,
    org.scalatest.scalatest % Test,
    org.scalatestplus.`mockito-5-10` % Test,
    com.typesafe.akka.`akka-stream-testkit` % Test,
    com.typesafe.akka.`akka-http-testkit` % Test,
  )
)
