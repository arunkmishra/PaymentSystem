package com.github.psp.paymentsystem.utils

import scala.concurrent._

import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models.Transaction

object PaymentLogger extends LazyLogging {
  def logTransaction(
    transaction: Transaction
  )(implicit
    ec: ExecutionContext
  ): Future[Unit] = Future {
    logger.debug(s"Processing transaction: [${transaction}]")
    // TODO: Actually instead of logging we can store these transaction in some storage which can be used later for debugging
  }
}
