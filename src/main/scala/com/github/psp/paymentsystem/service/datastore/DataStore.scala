package com.github.psp.paymentsystem.service.datastore

import scala.concurrent._

import com.github.psp.paymentsystem.models._

trait DataStore {
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

  def logBothStores: Unit
}
