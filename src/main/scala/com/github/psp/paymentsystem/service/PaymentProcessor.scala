package com.github.psp.paymentsystem.service

import scala.concurrent.Future

import com.github.psp.paymentsystem.models._

trait PaymentProcessor {
  def processPayment(request: PaymentRequest): Future[TransactionResponse]
  def printTransaction: Unit
}
