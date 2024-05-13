package com.github.psp.paymentsystem.routes

import java.time.YearMonth
import java.time.format.DateTimeFormatter

import scala.concurrent.Future

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest
import com.github.psp.paymentsystem.models.response._
import com.github.psp.paymentsystem.routes.PaymentRoutesSpec._
import com.github.psp.paymentsystem.service.PaymentProcessorService
import com.github.psp.paymentsystem.service.acquirer.MockAcquirerConnector
import com.github.psp.paymentsystem.service.datastore.InMemoryDataStore
import com.github.psp.paymentsystem.service.validator.CardExpired

class PaymentRoutesSpec
    extends AnyFlatSpec
       with Matchers
       with ScalatestRouteTest
       with MockitoSugar {
  private lazy val paymentProcessorService = PaymentProcessorService()
  private val paymentRoutes = new PaymentRoutes(paymentProcessorService).routes

  it should "return response as json when valid request is sent" in {
    Post("/v1/api/payment", ValidPaymentRequest) ~> paymentRoutes ~> check {
      status shouldBe StatusCodes.OK
      val expectedResponse = responseAs[TransactionResponse]
      expectedResponse.status shouldBe Approved
      expectedResponse.message shouldBe None
    }
  }

  it should "return response as json when invalid request is sent" in {
    Post("/v1/api/payment", InvalidPaymentRequest) ~> paymentRoutes ~> check {
      status shouldBe StatusCodes.OK
      val expectedResponse = responseAs[TransactionResponse]
      expectedResponse.status shouldBe Failed
      expectedResponse.message shouldBe Some(CardExpired().errorMessage)
    }
  }

  it should "return 500 response as json if something goes wrong" in {
    lazy val mockedDb = mock[InMemoryDataStore]
    when(mockedDb.addTransaction(any())(any()))
      .thenReturn(Future.failed(new Throwable("Cannot save in DB")))
    val paymentProcessor = new PaymentProcessorService(mockedDb, new MockAcquirerConnector())
    val paymentRoutes = new PaymentRoutes(paymentProcessor).routes
    Post("/v1/api/payment", InvalidPaymentRequest) ~> paymentRoutes ~> check {
      status shouldBe StatusCodes.InternalServerError
      val expectedResponse = responseAs[ErrorResponse]
      expectedResponse.message shouldBe PaymentRoutes.ErrorMessage
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
