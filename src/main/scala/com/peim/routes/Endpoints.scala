package com.peim.routes

import com.peim.HelloWorld.{Greeting, Name}
import com.peim.Jokes.Joke
import com.peim.dao.models.TagItem
import sttp.tapir.json.circe.jsonBody
import sttp.tapir._

object Endpoints {

  val getTagById: Endpoint[String, String, TagItem, Nothing] =
    endpoint
      .get
      .in("tags" / "get")
      .in(query[String]("id"))
      .out(jsonBody[TagItem])
      .errorOut(stringBody)

  val createTag: Endpoint[TagItem, String, Unit, Nothing] =
    endpoint
      .post
      .in("tags" / "create")
      .in(jsonBody[TagItem])
      .errorOut(stringBody)

  val getJoke: Endpoint[Unit, String, Joke, Nothing] =
    endpoint
      .get
      .in("joke")
      .out(jsonBody[Joke])
      .errorOut(stringBody)

//  val sayHello1: Endpoint[Name, String, Greeting, Nothing] =
//    endpoint
//      .get
//      .in("hello")
//      .in(query[String]("name").mapTo(Name))
//      .out(jsonBody[Greeting])
//      .errorOut(stringBody)
//
//  val sayHello2: Endpoint[Name, String, Greeting, Nothing] =
//    endpoint
//      .get
//      .in("hello" / path[String]("name").mapTo(Name))
//      .out(jsonBody[Greeting])
//      .errorOut(stringBody)

}
