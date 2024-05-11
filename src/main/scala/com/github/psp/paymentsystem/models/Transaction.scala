package com.github.psp.paymentsystem.models

import java.time.LocalDateTime

sealed case class Transaction(
  id: TransactionId,
  creditCard: CreditCard,
  amount: BigDecimal,
  status: TransactionStatus,
  merchantId: String,
  createdAt: LocalDateTime = LocalDateTime.now(),
  updatedAt: Option[LocalDateTime] = None,
  message: Option[String] = None,
) {
  def updateStatus(newStatus: TransactionStatus, message: Option[String] = None): Transaction =
    this.copy(status = newStatus, updatedAt = Some(LocalDateTime.now()), message = message)
}
object Transaction {
  def createInitialTransaction(request: PaymentRequest): Transaction =
    Transaction(
      TransactionId(),
      request.card,
      request.amount,
      Pending,
      request.merchantId,
      LocalDateTime.now(),
    )
}
