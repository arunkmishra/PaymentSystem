package com.github.psp.paymentsystem.service

import scala.concurrent._
import scala.util._

import com.typesafe.scalalogging.LazyLogging

trait BaseService extends LazyLogging {
  implicit val ec: ExecutionContext

  // TODO: create a logger which logs incoming requests into hadoop
  // def hadoopLogger: HadoopLogger

  // TODO: add reporting services to report metrics
  def measureAndLog[A, B](
    request: A,
    thePath: String,
    startTime: Long,
  )(
    service: A => Future[B]
  )(implicit
    ec: ExecutionContext
  ): Future[B] = {
    val processingFuture = service(request)
    processingFuture.onComplete {
      case Success(_) =>
        val responseTime = System.currentTimeMillis() - startTime
        logger.info(s"successfully finished request($thePath) in ${responseTime} ms")
      // log request into hadoop
      case Failure(err) =>
        val responseTime = System.currentTimeMillis() - startTime
        logger.error(s"Failed request($thePath) in $responseTime ms")
      // log request into hadoop
    }
    processingFuture
  }
}
