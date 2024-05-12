package com.github.psp.paymentsystem.service.datastore

import java.time.YearMonth

import scala.concurrent._
import scala.concurrent.duration.DurationInt

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.github.psp.paymentsystem.models._

class InMemoryDataStoreSpec extends AnyFlatSpec with Matchers {
  import InMemoryDataStoreSpec._

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  val store: InMemoryDataStore = new InMemoryDataStore()

  "addTransaction" should "add a transaction and return the transaction ID" in {
    val transaction = createTestTransaction()
    val addResultFuture = store.addTransaction(transaction)
    val addResult = Await.result(addResultFuture, 2.seconds)
    addResult.id shouldBe transaction.id.id
    store.fetchTransactionById(transaction.id) shouldBe Some(transaction)
  }

  val transaction: Transaction = createTestTransaction()
  val transactionId: TransactionId = transaction.id
  "updateTransaction" should "update the status of an existing transaction" in {
    val newStatus = Failed
    val newMessage = Some("Failed by acquirer")

    val resultFuture = for {
      _ <- store.addTransaction(transaction)
      _ <- store.updateTransaction(transactionId, newStatus, newMessage)
      fetched = store.fetchTransactionById(transactionId).get
    } yield fetched

    val fetched = Await.result(resultFuture, 2.seconds)
    fetched.status shouldEqual newStatus
    fetched.message shouldEqual newMessage
  }
  it should "return history for the updates" in {
    val history = store.fetchTransactionHistoryById(transactionId)
    history should not be None
    history.get should have size 2
    history.get.map(_.status) shouldBe List(Pending, Failed)
  }

  it should "return None if the transaction does not exist" in {
    store.fetchTransactionById(TransactionId()) shouldBe None
  }
}

object InMemoryDataStoreSpec {
  private val CreditCardNumber = "12345"
  private val Cvv = 111
  private val Amount = 111.11
  private val Currency = "USD"
  private val MerchantId = "123"
  private val TestPaymentRequest = PaymentRequest(
    CreditCard(CreditCardNumber, YearMonth.now),
    Cvv,
    Amount,
    Currency,
    MerchantId,
  )
  def createTestTransaction(): Transaction =
    Transaction.createInitialTransaction(TestPaymentRequest)
}
