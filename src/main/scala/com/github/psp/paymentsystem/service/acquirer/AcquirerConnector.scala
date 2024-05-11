package com.github.psp.paymentsystem.service.acquirer

import scala.concurrent.Future

import com.github.psp.paymentsystem.models._

trait AcquirerConnector {
  def sendToAcquirer(transaction: Transaction): Future[TransactionStatus]
}
