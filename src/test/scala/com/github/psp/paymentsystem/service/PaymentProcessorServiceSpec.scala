package com.github.psp.paymentsystem.service

import java.time.YearMonth

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.service.PaymentProcessorServiceSpec._
import com.github.psp.paymentsystem.service.acquirer.MockAcquirerConnector
import com.github.psp.paymentsystem.service.datastore.InMemoryDataStore
import com.github.psp.paymentsystem.service.validator.CardExpired

class PaymentProcessorServiceSpec extends AsyncFlatSpec with Matchers {
  val dataStore: InMemoryDataStore = new InMemoryDataStore()
  val acquirer: MockAcquirerConnector = new MockAcquirerConnector()

  val service: PaymentProcessorService = new PaymentProcessorService(dataStore, acquirer)

  it should "process a valid payment request successfully" in {
    service.processPayment(ValidPaymentRequest).map { result =>
      result.errorMessage shouldEqual None
      result.status shouldEqual Approved
    }
  }

  it should "handle an invalid payment request and reflect it properly" in {
    service.processPayment(InvalidPaymentRequest).map { result =>
      result.errorMessage.get shouldEqual CardExpired().errorMessage
      result.status shouldEqual Failed
    }
  }
}
object PaymentProcessorServiceSpec {
  private val CreditCardNumber = "3301022210769224"
  private val Cvv = 111
  private val Amount = 111.11
  private val Currency = "USD"
  private val MerchantId = "123456789012345"
  private val ValidPaymentRequest = PaymentRequest(
    CreditCard(CreditCardNumber, YearMonth.now().plusYears(1)),
    Cvv,
    Amount,
    Currency,
    MerchantId,
  )

  private val InvalidPaymentRequest = PaymentRequest(
    CreditCard(CreditCardNumber, YearMonth.now.minusMonths(2)),
    Cvv,
    Amount,
    Currency,
    MerchantId,
  )
}
