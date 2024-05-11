package com.github.psp.paymentsystem.service.acquirer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.github.psp.paymentsystem.models._

class MockAcquirerConnector extends AcquirerConnector {
  override def sendToAcquirer(transaction: Transaction): Future[TransactionStatus] =
    Future {
      Thread.sleep(5000)
      if (transaction.creditCard.cardNumber.last.asDigit % 2 != 0) Approved else Failed
    }
}
