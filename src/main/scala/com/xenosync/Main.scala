package com.xenosync

import akka.actor._
import akka.io.IO
import spray.can.Http
import spray.can.server._

object Boot extends App {
	implicit val system = ActorSystem("xenosync")
	val api = system.actorOf(Props(new RestInterface()), "httpinterface")
	IO(Http) ! Http.Bind(listener = api, interface = "localhost", port = 8080)
}