package com.github.psp.paymentsystem.models.request

import java.time.YearMonth
import java.time.format.DateTimeFormatter

import scala.util.Try

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
      CreditCard(
        cardNumber,
        Try(YearMonth.parse(expiryDate, formatter)).getOrElse(YearMonth.now()),
      ),
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
