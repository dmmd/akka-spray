package com.xenosync

import akka.actor._
import spray.routing.RequestContext
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._

class Responder(requestContext:RequestContext, ticketMaster:ActorRef) extends Actor with ActorLogging {
  import TicketProtocol._
  import spray.httpx.SprayJsonSupport._

  def receive = {
    case "Hello" => 
      requestContext.complete("<h1>EVENTS</h1")
      self ! PoisonPill
    
    case ticket:Ticket =>
      requestContext.complete(StatusCodes.OK, ticket)
      self ! PoisonPill

    case EventCreated =>
      requestContext.complete(StatusCodes.OK)
      self ! PoisonPill

    case SoldOut =>
      requestContext.complete(StatusCodes.NotFound)
      self ! PoisonPill

    case Events(events) =>
      requestContext.complete(StatusCodes.OK, events)
      self ! PoisonPill

  }
}

class NullActor extends Actor {
  def receive = {
    case msg => {
      println("Hello")
      sender ! "Hello from actor"
    }
  }
}

class EchoActor extends Actor {
  def receive = {
    case msg => sender ! msg
  }
}