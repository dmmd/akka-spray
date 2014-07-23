package com.xenosync

import akka.actor._

import spray.routing._
import spray.http._
import MediaTypes._

class RestInterface extends HttpServiceActor with RestApi {
  def receive = runRoute(routes)
}

trait RestApi extends HttpService {

  def routes: Route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
          	<h1>Hello, there!</h1>
          }
        }
      }
    }
}