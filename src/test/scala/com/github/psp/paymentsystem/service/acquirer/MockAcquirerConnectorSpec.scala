package com.github.psp.paymentsystem.service.acquirer

import java.time.YearMonth

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.models._

class MockAcquirerConnectorSpec extends AsyncFlatSpec with Matchers {
  import MockAcquirerConnectorSpec._

  val acquirerConnector: MockAcquirerConnector = new MockAcquirerConnector()
  "sendToAcquirer" should "approve the transaction if the last digit of the card number is odd" in {
    val transaction = createTestTransaction(CreditWithOddLastDigit)
    acquirerConnector.sendToAcquirer(transaction).map { status =>
      status shouldEqual Failed
    }
  }

  it should "fail the transaction if the last digit of the card number is even" in {
    val transaction = createTestTransaction(CreditWithEvenLastDigit)
    acquirerConnector.sendToAcquirer(transaction).map { status =>
      status shouldEqual Approved
    }
  }
}
object MockAcquirerConnectorSpec {
  private val CreditWithEvenLastDigit = CreditCard("1234567890123456", YearMonth.now())
  private val CreditWithOddLastDigit = CreditCard("1234567890123457", YearMonth.now())
  private val Cvv = 111
  private val Amount = 111.11
  private val Currency = "USD"
  private val MerchantId = "123"
  def createTestTransaction(creditCard: CreditCard): Transaction =
    Transaction.createInitialTransaction(
      PaymentRequest(
        creditCard,
        Cvv,
        Amount,
        Currency,
        MerchantId,
      )
    )
}
