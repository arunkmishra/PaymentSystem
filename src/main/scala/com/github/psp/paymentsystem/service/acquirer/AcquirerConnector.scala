package com.github.psp.paymentsystem.service.acquirer

import scala.concurrent.Future

import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models._

trait AcquirerConnector extends LazyLogging {
  def sendToAcquirer(transaction: Transaction): Future[TransactionStatus]
}
