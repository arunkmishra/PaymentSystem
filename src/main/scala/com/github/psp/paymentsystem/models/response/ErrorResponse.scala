package com.github.psp.paymentsystem.models.response

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class ErrorResponse(message: String)
object ErrorResponse {
  implicit val transactionResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat1(
    ErrorResponse.apply
  )
}
