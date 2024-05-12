package com.github.psp.paymentsystem.models

import com.github.psp.paymentsystem.service.validator.ValidationError
import com.github.psp.paymentsystem.service.validator.Validator._

case class PaymentRequest(
  card: CreditCard,
  cvv: Int,
  amount: BigDecimal,
  currency: String,
  merchantId: String,
) {
  def isValidRequest: Either[ValidationError, PaymentRequest] =
    for {
      _ <- card.isValidCard
      _ <- validateAmount(amount)
      _ <- validateCvv(cvv)
      _ <- validateCurrency(currency)
      _ <- validateMerchantId(merchantId)
    } yield this
}
