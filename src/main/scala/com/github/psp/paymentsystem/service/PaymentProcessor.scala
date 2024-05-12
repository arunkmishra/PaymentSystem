package com.github.psp.paymentsystem.service

import scala.concurrent.Future

import com.typesafe.scalalogging.LazyLogging

import com.github.psp.paymentsystem.models._
import com.github.psp.paymentsystem.models.response.TransactionResponse

trait PaymentProcessor extends LazyLogging {
  def processPayment(request: PaymentRequest): Future[TransactionResponse]
  def printTransaction: Unit
}
