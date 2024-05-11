package com.github.psp.paymentsystem.models.request

import java.time.YearMonth
import java.time.format.DateTimeFormatter

import spray.json._

import com.github.psp.paymentsystem.models._

case class ProcessPaymentRequest(
  cardNumber: String,
  expiryDate: String,
  cvv: Int,
  amount: BigDecimal,
  currency: String,
  merchantId: String,
) {
  import ProcessPaymentRequest._
  def toPaymentRequest: PaymentRequest =
    PaymentRequest(
      CreditCard(cardNumber, YearMonth.parse(expiryDate, formatter)),
      cvv,
      amount,
      currency,
      merchantId,
    )
}
object ProcessPaymentRequest extends DefaultJsonProtocol {
  implicit val processPaymentRequestFormat: RootJsonFormat[ProcessPaymentRequest] = jsonFormat6(
    ProcessPaymentRequest.apply
  )
  private val formatter = DateTimeFormatter.ofPattern("MM/yy")
}
