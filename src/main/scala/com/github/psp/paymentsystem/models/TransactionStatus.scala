package com.github.psp.paymentsystem.models

import spray.json._

sealed trait TransactionStatus

case object Pending extends TransactionStatus
case object Processing extends TransactionStatus
case object Approved extends TransactionStatus
case object Failed extends TransactionStatus

object TransactionStatus {
  implicit object TransactionStatusFormat extends RootJsonFormat[TransactionStatus] {
    def write(status: TransactionStatus): JsValue = status match {
      case Pending => JsString("Pending")
      case Processing => JsString("Processing")
      case Approved => JsString("Approved")
      case Failed => JsString("Failed")
    }

    def read(json: JsValue): TransactionStatus = json match {
      case JsString("Pending") => Pending
      case JsString("Processing") => Processing
      case JsString("Approved") => Approved
      case JsString("Failed") => Failed
      case other => deserializationError(s"Invalid value for TransactionStatus: $other")
    }
  }
}
