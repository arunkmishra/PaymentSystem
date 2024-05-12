package com.github.psp.paymentsystem.service.validator

sealed trait ValidationError {
  def errorMessage: String
}
case class InvalidCardNumber(errorMessage: String = "Invalid card number") extends ValidationError
case class CardExpired(errorMessage: String = "Card expired") extends ValidationError
case class InvalidAmount(errorMessage: String = "Amount is not more than 0") extends ValidationError
case class InvalidMerchantId(errorMessage: String = "Invalid merchant Id") extends ValidationError
case class InvalidCvv(errorMessage: String = "Invalid cvv") extends ValidationError
case class InvalidCurrency(errorMessage: String = "Invalid currency code") extends ValidationError
