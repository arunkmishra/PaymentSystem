package com.github.psp.paymentsystem

import scala.concurrent._
import scala.concurrent.duration.Duration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models.AppConfig
import com.github.psp.paymentsystem.routes._
import com.github.psp.paymentsystem.service.PaymentProcessorService

object PaymentSystemBoot extends LazyLogging with App {
  implicit val system: ActorSystem = ActorSystem("payment-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private lazy val appConfig = AppConfig.fromConfig()
  private lazy val paymentProcessorService: PaymentProcessorService = PaymentProcessorService()

  private def startServer(): Future[Http.ServerBinding] = {
    val systemRoutes = new ServiceRoutes().routes
    val paymentRoutes = new PaymentRoutes(paymentProcessorService).routes
    val bindingFuture =
      Http().newServerAt("0.0.0.0", appConfig.port).bind(systemRoutes ~ paymentRoutes)
    logger.info(s"Server now online. Navigate to http://localhost:${appConfig.port}/status")
    bindingFuture
  }

  private val serverBinding = startServer()

  sys.addShutdownHook {
    logger.info("Received shutdown signal.")
    serverBinding
      .flatMap(_.unbind())
      .onComplete { _ =>
        system.terminate()
        logger.info("Server and Actor System terminated.")
      }
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
