package com.github.psp.paymentsystem.models

import com.typesafe.config._

case class AppConfig(port: Int)

object AppConfig {
  def fromConfig(config: Config = ConfigFactory.load()): AppConfig =
    AppConfig(config.getInt("payment.http.port"))
}
