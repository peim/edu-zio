package com.peim

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.implicits._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.blaze._

import org.http4s.server.middleware.Logger
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext.global

object Main extends App {

  val server: ZIO[ZEnv, Throwable, Unit] = ZIO.runtime[ZEnv]
    .flatMap {
      implicit rts =>
        val api = {
          for {
            client <- BlazeClientBuilder[Task](global).stream
            helloWorldAlg = HelloWorld.impl[Task]
            jokeAlg = Jokes.impl[Task](client)

            // Combine Service Routes into an HttpApp.
            // Can also be done via a Router if you
            // want to extract a segments not checked
            // in the underlying routes.

            routes = new EdubaseRoutes[Task]

            httpApp = (
              routes.swaggerDocsRoutes <+>
                routes.sayHello1Routes(helloWorldAlg) <+>
                routes.sayHello2Routes(helloWorldAlg) <+>
                routes.jokeRoutes(jokeAlg)
              ).orNotFound

            // With Middlewares in place
            finalHttpApp = Logger.httpApp(true, true)(httpApp)

            exitCode <- BlazeServerBuilder[Task]
              .bindHttp(8080, "0.0.0.0")
              .withHttpApp(finalHttpApp)
              .serve
          } yield exitCode
        }
        api.compile.drain
    }

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    server.fold(_ => 1, _ => 0)

}