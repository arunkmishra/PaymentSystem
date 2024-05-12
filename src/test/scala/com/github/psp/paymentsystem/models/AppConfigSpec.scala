package com.github.psp.paymentsystem.utils.models

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.models.AppConfig

class AppConfigSpec extends AnyFlatSpec with Matchers {
  import AppConfigSpec._

  it should "correctly fetch port number from the config correctly" in {
    val config = ConfigFactory.parseString(Config)
    AppConfig.fromConfig(config).port shouldBe PortNumber
  }
}
object AppConfigSpec {
  private val PortNumber = 8000
  private val Config =
    s"""
      |payment.http {
      |  port = ${PortNumber}
      |}
      |""".stripMargin
}
