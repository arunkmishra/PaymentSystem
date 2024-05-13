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
        val `akka-stream-testkit` = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion
        val `akka-http-testkit` = "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion
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

  object org {
    object scalatest {
      private val ScalatestVersion = "3.2.18"
      val scalatest = "org.scalatest" %% "scalatest" % ScalatestVersion
    }
    object scalatestplus {
      private val ScalatestVersion = "3.2.18.0"
      val `mockito-5-10` = "org.scalatestplus" %% "mockito-5-10" % ScalatestVersion
    }
  }
}
