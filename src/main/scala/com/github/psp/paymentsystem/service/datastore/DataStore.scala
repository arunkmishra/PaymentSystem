package com.github.psp.paymentsystem.service.datastore

import scala.concurrent._

import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models._

trait DataStore extends LazyLogging {
  def addTransaction(
    transaction: Transaction
  )(implicit
    ec: ExecutionContext
  ): Future[TransactionId]

  def updateTransaction(
    transactionId: TransactionId,
    newStatus: TransactionStatus,
    message: Option[String] = None,
  )(implicit
    ec: ExecutionContext
  ): Future[Option[Transaction]]

  def fetchTransactionById(transactionId: TransactionId): Option[Transaction]

  def fetchTransactionHistoryById(transactionId: TransactionId): Option[List[Transaction]]

  def logBothStores: Unit
}
