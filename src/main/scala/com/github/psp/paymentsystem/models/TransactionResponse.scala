package com.github.psp.paymentsystem.models

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
case class TransactionResponse(
  transactionId: TransactionId,
  status: TransactionStatus,
  errorMessage: Option[String],
)

object TransactionResponse {
  implicit val transactionResponseFormat: RootJsonFormat[TransactionResponse] = jsonFormat3(
    TransactionResponse.apply
  )
}
