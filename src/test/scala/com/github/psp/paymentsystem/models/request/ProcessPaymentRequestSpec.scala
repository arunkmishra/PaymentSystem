package com.github.psp.paymentsystem.utils.models.request

import java.time.YearMonth

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.models.request.ProcessPaymentRequest

class ProcessPaymentRequestSpec extends AnyFlatSpec with Matchers {
  import ProcessPaymentRequestSpec._

  it should "return payment request properly" in {
    val paymentRequest = DummyProcessPaymentRequest.toPaymentRequest
    val expectedRequest = PaymentRequest(
      CreditCard(CreditCardNumber, YearMonth.of(2020, 10)),
      Cvv,
      Amount,
      Currency,
      MerchantId,
    )
    paymentRequest shouldBe expectedRequest
  }
}
object ProcessPaymentRequestSpec {
  private val CreditCardNumber = "12345"
  private val ExpiryDate = "10/20"
  private val Cvv = 111
  private val Amount = 111.11
  private val Currency = "USD"
  private val MerchantId = "123"
  private val DummyProcessPaymentRequest =
    ProcessPaymentRequest(CreditCardNumber, ExpiryDate, Cvv, Amount, Currency, MerchantId)
}
