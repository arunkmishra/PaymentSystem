import sbt._

object Dependencies {
  object com {
    object typesafe {
      object akka {
        private val AkkaVersion = "2.9.0"
        private val AkkaHttpVersion = "10.6.0"
        val `akka-actor-typed` = "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
        val `akka-stream` = "com.typesafe.akka" %% "akka-stream" % AkkaVersion
        val `akka-http` = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
        val `akka-http-spray-json` = "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
      }

      object `scala-logging` {
        private val ScalaLoggingVersion = "3.9.5"
        val `scala-logging` =
          "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion
      }
    }
  }

  object ch {
    object qos {
      object logback {
        private val LogbackVersion = "1.5.6"
        val `logback-classic` =
          "ch.qos.logback" % "logback-classic" % LogbackVersion
      }
    }
  }
}
