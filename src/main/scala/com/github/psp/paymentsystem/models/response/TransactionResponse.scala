package com.github.psp.paymentsystem.models.response

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import com.github.psp.paymentsystem.models._
case class TransactionResponse(
  transactionId: TransactionId,
  status: TransactionStatus,
  message: Option[String],
)

object TransactionResponse {
  implicit val transactionResponseFormat: RootJsonFormat[TransactionResponse] = jsonFormat3(
    TransactionResponse.apply
  )
}
