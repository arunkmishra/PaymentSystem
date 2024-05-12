package com.github.psp.paymentsystem.service.validator

import java.time.YearMonth

import com.github.psp.paymentsystem.utils.CardUtils

object Validator {
  private val validCurrencies = Seq("USD", "INR", "THB", "CHF", "EUR")
  def validateCardNumber(cardNumber: String): Either[ValidationError, String] =
    if (CardUtils.validateCardNumber(cardNumber)) Right(cardNumber)
    else Left(InvalidCardNumber())

  def validateExpiryDate(expiryDate: YearMonth): Either[ValidationError, YearMonth] =
    if (expiryDate.isAfter(YearMonth.now())) Right(expiryDate)
    else Left(CardExpired())

  def validateAmount(amount: BigDecimal): Either[ValidationError, BigDecimal] =
    if (amount > 0) Right(amount)
    else Left(InvalidAmount())

  def validateMerchantId(merchantId: String): Either[ValidationError, String] =
    if (merchantId.length == 15) Right(merchantId)
    else Left(InvalidMerchantId())

  // cvv number can be 3 or 4 digit long check
  def validateCvv(cvv: Int): Either[ValidationError, Int] =
    if (cvv > 99 && cvv < 10000) Right(cvv)
    else Left(InvalidCvv())

  def validateCurrency(currency: String): Either[ValidationError, String] =
    if (validCurrencies.contains(currency)) Right(currency)
    else Left(InvalidCurrency())
}
