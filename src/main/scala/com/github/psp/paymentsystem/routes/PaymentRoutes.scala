package com.github.psp.paymentsystem.routes

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest
import com.github.psp.paymentsystem.models.response.TransactionResponse
import com.github.psp.paymentsystem.service._

class PaymentRoutes(paymentProcessor: PaymentProcessor) extends BaseService {
  override implicit val ec: ExecutionContext = ExecutionContext.global

  private def processPayment: Route =
    pathPrefix("v1" / "api") {
      post {
        path("payment") {
          val startTime = System.currentTimeMillis()
          entity(as[ProcessPaymentRequest]) { paymentRequest =>
            val paymentPath = "/v1/api/payment"
            val request = paymentRequest.toPaymentRequest
            val response = measureAndLog[ProcessPaymentRequest, TransactionResponse](
              paymentRequest,
              paymentPath,
              startTime,
            )(_ => paymentProcessor.processPayment(request))
            complete(response)
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
