package com.github.psp.paymentsystem.service

import scala.concurrent._

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.service.acquirer._
import com.github.psp.paymentsystem.service.datastore._

class PaymentProcessorService(dataStore: DataStore, acquirer: AcquirerConnector)
    extends PaymentProcessor {
  implicit val ec: ExecutionContext = ExecutionContext.global
  override def processPayment(request: PaymentRequest): Future[TransactionResponse] = {
    val initialTransaction = Transaction.createInitialTransaction(request)
    val transactionId = initialTransaction.id
    dataStore.addTransaction(initialTransaction).flatMap { _ =>
      request
        .isValidRequest
        .fold(
          validationError =>
            dataStore
              .updateTransaction(transactionId, Failed, Some(validationError.errorMessage))
              .map { _ =>
                TransactionResponse(transactionId, Failed, Some(validationError.errorMessage))
              },
          _ => {
            val processingTransaction = initialTransaction.updateStatus(Processing)
            dataStore.updateTransaction(transactionId, Processing)
            val processing = acquirer.sendToAcquirer(processingTransaction)

            processing.flatMap { transactionStatus =>
              val message = if (transactionStatus == Failed) Some("Failed by acquirer") else None
              dataStore
                .updateTransaction(
                  transactionId,
                  transactionStatus,
                  message,
                )
                .map(_ => TransactionResponse(transactionId, transactionStatus, message))
            }
          },
        )
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
