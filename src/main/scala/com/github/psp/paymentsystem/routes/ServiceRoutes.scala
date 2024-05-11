package com.github.psp.paymentsystem.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class ServiceRoutes {
  val routes: Route = path("status")(get(complete("OK")))
}
