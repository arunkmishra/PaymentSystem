package com.github.psp.paymentsystem.routes

import scala.concurrent.ExecutionContext
import scala.util._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest
import com.github.psp.paymentsystem.models.response._
import com.github.psp.paymentsystem.service._

class PaymentRoutes(
  paymentProcessor: PaymentProcessor
)(implicit
  val ec: ExecutionContext
) extends BaseService {
  import PaymentRoutes._
  private def processPayment: Route =
    pathPrefix("v1" / "api") {
      post {
        path("payment") {
          val startTime = System.currentTimeMillis()
          entity(as[ProcessPaymentRequest]) { paymentRequest =>
            val paymentPath = "/v1/api/payment"
            val request = paymentRequest.toPaymentRequest
            logger.info(
              s"Request to process payment for amount ${request.amount} to merchantId[${request.merchantId}]"
            )
            val response = measureAndLog[ProcessPaymentRequest, TransactionResponse](
              paymentRequest,
              paymentPath,
              startTime,
            )(_ => paymentProcessor.processPayment(request))
            onComplete(response) {
              case Success(value) =>
                logger.info(
                  s"Processed payment request for amount ${request.amount} to merchantId[${request.merchantId}] " +
                    s"with transactionId[${value.transactionId}] and status ${value.status}"
                )
                complete(value)
              case Failure(err) =>
                logger.error(
                  s"Failed to process payment request for amount ${request.amount} to merchantId[${request.merchantId}]",
                  err,
                )
                complete(
                  StatusCodes.InternalServerError -> ErrorResponse(ErrorMessage)
                )
            }
          }
        }
      }
    }

  val routes: Route = processPayment
}
object PaymentRoutes {
  val ErrorMessage: String = "Failed to process request"
}
