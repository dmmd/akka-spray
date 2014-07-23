package com.xenosync

import akka.actor._
import akka.pattern.{ ask, pipe }
import akka.util.Timeout

import scala.concurrent.duration._
import spray.routing._
import spray.http._

import MediaTypes._
import TicketProtocol._


class RestInterface extends HttpServiceActor with RestApi {
  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging { actor: Actor =>
  import context.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val echoActor = context.actorOf(Props[EchoActor])

  def routes: Route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
          	<h1>Hello, there!</h1>
          }
        }
      }
    } ~
    path("events"){
      get { requestContext =>
        val responder = createResponder(requestContext)
        echoActor.ask("Hello").pipeTo(responder)
      }
    }

  def createResponder(requestContext:RequestContext) = {
    context.actorOf(Props(new Responder(requestContext, echoActor)))
  }
}