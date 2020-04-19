package com.peim

import cats.effect.{ContextShift, Sync, Timer}
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import sttp.tapir.server.http4s._
import sttp.tapir.swagger.http4s.SwaggerHttp4s

class EdubaseRoutes[F[_]: Sync](implicit T: Timer[F], C: ContextShift[F]) extends Http4sDsl[F] {

  def jokeRoutes(J: Jokes[F]): HttpRoutes[F] = {
    Endpoints.getJoke.toRoutes(_ => J.get)
  }

  def sayHello1Routes(H: HelloWorld[F]): HttpRoutes[F] = {
    Endpoints.sayHello1.toRoutes(name => H.hello(name))
  }

  def sayHello2Routes(H: HelloWorld[F]): HttpRoutes[F] = {
    Endpoints.sayHello2.toRoutes(name => H.hello(name))
  }

  def swaggerDocsRoutes: HttpRoutes[F] = {
    import sttp.tapir.docs.openapi._
    import sttp.tapir.openapi.circe.yaml._

    val docs = List(
      Endpoints.getJoke,
      Endpoints.sayHello1,
      Endpoints.sayHello2
    ).toOpenAPI("Hello world docs", "1.0")
    new SwaggerHttp4s(docs.toYaml).routes
  }
}