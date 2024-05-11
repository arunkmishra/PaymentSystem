package com.github.psp.paymentsystem.models

import java.util.UUID

import scala.util.Try

import spray.json._

sealed case class TransactionId(id: String)
object TransactionId {
  def apply(): TransactionId = TransactionId(UUID.randomUUID().toString)
  implicit object transactionIdFormat extends RootJsonFormat[TransactionId] {
    override def read(json: JsValue): TransactionId = json match {
      case JsString(s) if Try(UUID.fromString(s)).isSuccess =>
        TransactionId(UUID.fromString(s).toString)
      case _ => deserializationError("cannot deserialize transaction id")
    }
    override def write(transactionId: TransactionId): JsValue = JsString(transactionId.id)
  }
}
