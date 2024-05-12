package com.github.psp.paymentsystem.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import com.github.psp.paymentsystem.models.{Approved, Failed}
import com.github.psp.paymentsystem.models.response.TransactionResponse
import com.github.psp.paymentsystem.routes.PaymentRoutesSpec.{InvalidPaymentRequest, ValidPaymentRequest}
import com.github.psp.paymentsystem.service.PaymentProcessorService
import com.github.psp.paymentsystem.service.validator.CardExpired
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.YearMonth
import java.time.format.DateTimeFormatter

class PaymentRoutesSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {
  private lazy val paymentProcessorService = PaymentProcessorService()
  private val paymentRoutes = new PaymentRoutes(paymentProcessorService).routes

  it should "return response as json when valid request is sent" in {
    Post("/v1/api/payment", ValidPaymentRequest) ~> paymentRoutes ~> check {
      status shouldBe StatusCodes.OK
      val expectedResponse = responseAs[TransactionResponse]
      expectedResponse.status shouldBe Approved
      expectedResponse.errorMessage shouldBe None
    }
  }

  it should "return response as json when invalid request is sent" in {
    Post("/v1/api/payment", InvalidPaymentRequest) ~> paymentRoutes ~> check {
      status shouldBe StatusCodes.OK
      val expectedResponse = responseAs[TransactionResponse]
      expectedResponse.status shouldBe Failed
      expectedResponse.errorMessage shouldBe Some(CardExpired().errorMessage)
    }
  }
}
object PaymentRoutesSpec {
  private val CreditCardNumber = "3301022210769224"
  private val Cvv = 111
  private val Amount = 111.11
  private val Currency = "USD"
  private val MerchantId = "123456789012345"
  private val ValidPaymentRequest = ProcessPaymentRequest(
    CreditCardNumber,
    YearMonth.now().plusMonths(2).format(DateTimeFormatter.ofPattern("MM/yy")),
    Cvv,
    Amount,
    Currency,
    MerchantId,
  )
  private val InvalidPaymentRequest = ProcessPaymentRequest(
    CreditCardNumber,
    YearMonth.now().minusMonths(2).format(DateTimeFormatter.ofPattern("MM/yy")),
    Cvv,
    Amount,
    Currency,
    MerchantId,
  )
}
