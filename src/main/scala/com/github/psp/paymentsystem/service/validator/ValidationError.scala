package com.github.psp.paymentsystem.service.validator

sealed trait ValidationError {
  def errorMessage: String
}
case class InvalidCardNumber(errorMessage: String = "Invalid card number") extends ValidationError
case class CardExpired(errorMessage: String = "Card expired") extends ValidationError
case class InvalidAmount(errorMessage: String = "Amount is not more than 0") extends ValidationError
