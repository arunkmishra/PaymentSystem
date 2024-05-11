package com.github.psp.paymentsystem.service.validator

import java.time.YearMonth

import com.github.psp.paymentsystem.utils.CardUtils

object Validator {
  def validateCardNumber(cardNumber: String): Either[ValidationError, String] =
    if (CardUtils.validateCardNumber(cardNumber)) Right(cardNumber)
    else Left(InvalidCardNumber())

  def validateExpiryDate(expiryDate: YearMonth): Either[ValidationError, YearMonth] =
    if (expiryDate.isAfter(YearMonth.now())) Right(expiryDate)
    else Left(CardExpired())

  def validateAmount(amount: BigDecimal): Either[ValidationError, BigDecimal] =
    if (amount > 0) Right(amount)
    else Left(InvalidAmount())
}
