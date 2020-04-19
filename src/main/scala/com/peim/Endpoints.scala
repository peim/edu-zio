package com.peim

import com.peim.HelloWorld.{Greeting, Name}
import com.peim.HelloWorld.{Greeting, Name}
import com.peim.Jokes.Joke
import io.circe.generic.auto._
import sttp.tapir.Endpoint
import sttp.tapir._
import sttp.tapir.json.circe._

object Endpoints {

  val getJoke: Endpoint[Unit, String, Joke, Nothing] =
    endpoint
      .get
      .in("joke")
      .out(jsonBody[Joke])
      .errorOut(stringBody)

  val sayHello1: Endpoint[Name, String, Greeting, Nothing] =
    endpoint
      .get
      .in("hello")
      .in(query[String]("name").mapTo(Name))
      .out(jsonBody[Greeting])
      .errorOut(stringBody)

  val sayHello2: Endpoint[Name, String, Greeting, Nothing] =
    endpoint
      .get
      .in("hello" / path[String].mapTo(Name).name("name"))
      .out(jsonBody[Greeting])
      .errorOut(stringBody)

}
