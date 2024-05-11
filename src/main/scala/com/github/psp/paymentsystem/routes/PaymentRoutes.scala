package com.github.psp.paymentsystem.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest
import com.github.psp.paymentsystem.service.PaymentProcessor

class PaymentRoutes(paymentProcessor: PaymentProcessor) {
  private def processPayment: Route =
    pathPrefix("v1" / "api") {
      post {
        path("payment") {
          entity(as[ProcessPaymentRequest]) { paymentRequest =>
            val request = paymentRequest.toPaymentRequest
            complete(paymentProcessor.processPayment(request))
          }
        }
      } ~ {
        path("get") {
          get {
            paymentProcessor.printTransaction
            complete("OK")
          }
        }
      }
    }

  val routes: Route = processPayment
}
