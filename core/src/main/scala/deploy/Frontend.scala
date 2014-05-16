package io.prediction.core.deploy

import scala.concurrent.duration._
import akka.actor.Actor
import akka.contrib.pattern.DistributedPubSubExtension
import akka.contrib.pattern.DistributedPubSubMediator.Send
import akka.pattern._
import akka.util.Timeout

import io.prediction.core.deploy.master._

object Frontend extends Startup {
  case object Ok
  case object NotOk

  def main(args: Array[String]): Unit = {
    val actorSystem = startFrontend(args(0), args(1).toInt)
    actorSystem.awaitTermination()
  }
}

class Frontend extends Actor {
  import Frontend._
  import context.dispatcher
  val mediator = DistributedPubSubExtension(context.system).mediator

  def receive = {
    case work =>
      implicit val timeout = Timeout(5.seconds)
      (mediator ? Send("/user/master/active", work, localAffinity = false)) map {
        case Master.Ack(_) => Ok
      } recover { case _ => NotOk } pipeTo sender

  }

}
