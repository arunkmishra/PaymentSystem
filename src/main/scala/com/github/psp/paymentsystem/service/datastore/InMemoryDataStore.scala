package com.github.psp.paymentsystem.service.datastore

import scala.collection.mutable
import scala.concurrent._

import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models._

sealed class InMemoryDataStore extends DataStore with LazyLogging {
  private val Transactions: mutable.Map[TransactionId, Transaction] = mutable.Map()
  private val AllTransactionsStore: mutable.Map[TransactionId, List[Transaction]] = mutable.Map()

  private def logTransactionChange(
    transaction: Transaction
  )(implicit
    ec: ExecutionContext
  ): Unit = Future {
    val newTransactions =
      AllTransactionsStore.get(transaction.id).fold(List(transaction)) { allTransactions =>
        allTransactions :+ transaction
      }
    AllTransactionsStore(transaction.id) = newTransactions
  }

  private def fetchTransactionById(transactionId: TransactionId): Option[Transaction] =
    Transactions.get(transactionId)
  override def addTransaction(
    transaction: Transaction
  )(implicit
    ec: ExecutionContext
  ): Future[TransactionId] =
    Future {
      logTransactionChange(transaction)
      synchronized {
        Transactions(transaction.id) = transaction
        transaction.id
      }
    }

  def updateTransaction(
    transactionId: TransactionId,
    newStatus: TransactionStatus,
    message: Option[String] = None,
  )(implicit
    ec: ExecutionContext
  ): Future[Option[Transaction]] =
    Future {
      fetchTransactionById(transactionId).map { fetchedTransaction =>
        logger.debug(
          s"Updating transaction status from ${fetchedTransaction.status} to $newStatus for transactionId[$transactionId]"
        )
        val updatedTransaction = fetchedTransaction.updateStatus(newStatus, message)
        logTransactionChange(updatedTransaction)
        Transactions.update(
          transactionId,
          updatedTransaction,
        )
        updatedTransaction
      }
    }

  def logBothStores: Unit = {
    logger.info("-" * 20)
    logger.info("Transactions:\n" + Transactions.mkString("\n"))
    logger.info("-" * 20)
    logger.info("All transactions:\n" + AllTransactionsStore.mkString("\n"))
    logger.info("-" * 20)

  }
}
