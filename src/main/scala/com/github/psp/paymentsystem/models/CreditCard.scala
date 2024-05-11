package com.github.psp.paymentsystem.models

import java.time.YearMonth

import com.github.psp.paymentsystem.service.validator.ValidationError
import com.github.psp.paymentsystem.service.validator.Validator._

case class CreditCard(cardNumber: String, expiration: YearMonth) {
  def isValidCard: Either[ValidationError, CreditCard] =
    for {
      validCard <- validateCardNumber(cardNumber)
      validDate <- validateExpiryDate(expiration)
    } yield CreditCard(validCard, validDate)
}
