package com.github.psp.paymentsystem.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServiceRoutesSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {
  val serviceRoutes: Route = new ServiceRoutes().routes

  it should "return OK for get requests" in {
    Get("/status") ~> serviceRoutes ~> check {
      responseAs[String] shouldBe "OK"
    }
  }
}
