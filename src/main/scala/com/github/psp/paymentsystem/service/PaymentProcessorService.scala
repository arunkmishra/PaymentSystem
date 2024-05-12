package com.github.psp.paymentsystem.service

import scala.concurrent._

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.models.response.TransactionResponse
import com.github.psp.paymentsystem.service.acquirer._
import com.github.psp.paymentsystem.service.datastore._
import com.github.psp.paymentsystem.service.validator.ValidationError

class PaymentProcessorService(dataStore: DataStore, acquirer: AcquirerConnector)
    extends PaymentProcessor {
  implicit val ec: ExecutionContext = ExecutionContext.global
  override def processPayment(request: PaymentRequest): Future[TransactionResponse] = {
    val initialTransaction = Transaction.createInitialTransaction(request)
    val transactionId = initialTransaction.id
    logger.info(s"Processing payment for TransactionId[$transactionId]")
    dataStore.addTransaction(initialTransaction).flatMap { _ =>
      request
        .isValidRequest
        .fold(
          validationError => handleInvalidPaymentRequest(transactionId, validationError),
          _ => processPaymentForValidRequest(initialTransaction),
        )
    }
  }

  private def handleInvalidPaymentRequest(transactionId: TransactionId, error: ValidationError)
    : Future[TransactionResponse] = {
    logger.warn(s"Transaction[$transactionId] failed due to validation failures")
    dataStore
      .updateTransaction(transactionId, Failed, Some(error.errorMessage))
      .map { _ =>
        response
          .TransactionResponse(transactionId, Failed, Some(error.errorMessage))
      }
  }

  private def processPaymentForValidRequest(transaction: Transaction)
    : Future[TransactionResponse] = {
    val processingTransaction = transaction.updateStatus(Processing)
    dataStore.updateTransaction(transaction.id, Processing)
    val processing = acquirer.sendToAcquirer(processingTransaction)

    processing.flatMap { transactionStatus =>
      val message = if (transactionStatus == Failed) Some("Failed by acquirer") else None
      dataStore
        .updateTransaction(
          transaction.id,
          transactionStatus,
          message,
        )
        .map { _ =>
          logger.info(
            s"Finished transaction processing with status: $transactionStatus for transactionId[${transaction.id}]"
          )
          response.TransactionResponse(transaction.id, transactionStatus, message)
        }
    }
  }

  override def printTransaction: Unit = dataStore.logBothStores
}

object PaymentProcessorService {
  def apply(): PaymentProcessorService =
    new PaymentProcessorService(
      new InMemoryDataStore,
      new MockAcquirerConnector,
    )
}
