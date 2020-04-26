//package com.peim.routes
//
//import com.peim.dao.models.TagItem
//import com.peim.domain.TagsService
//import org.http4s.HttpRoutes
//import org.http4s.dsl.Http4sDsl
//import sttp.tapir.server.http4s._
//import sttp.tapir.swagger.http4s.SwaggerHttp4s
//import zio.Task
//import zio.interop.catz._
//
//final case class TagsRoutes2(tagsService: TagsService.Service) extends Http4sDsl[Task] {
//
//  def getById(id: String) = {
//    Endpoints.getTagById.toRoutes(id => tagsService.getById(id))
//  }
//
//  def createTag(tag: TagItem) = {
//    Endpoints.createTag.toRoutes(tag => tagsService.create(tag))
//  }
//
//  //  def jokeRoutes(J: Jokes[F]): HttpRoutes[F] = {
//  //    Endpoints.getJoke.toRoutes(_ => J.get)
//  //  }
//  //
//  //  def sayHello1Routes(H: HelloWorld[F]): HttpRoutes[F] = {
//  //    Endpoints.sayHello1.toRoutes(name => H.hello(name))
//  //  }
//  //
//  //  def sayHello2Routes(H: HelloWorld[F]): HttpRoutes[F] = {
//  //    Endpoints.sayHello2.toRoutes(name => H.hello(name))
//  //  }
//  //
//  def swaggerDocsRoutes: HttpRoutes[Task] = {
//    import sttp.tapir.docs.openapi._
//    import sttp.tapir.openapi.circe.yaml._
//
//    val docs = List(
//      Endpoints.getTagById,
//      Endpoints.createTag
////      Endpoints.getJoke,
////      Endpoints.sayHello1,
////      Endpoints.sayHello2
//    ).toOpenAPI("Hello world docs", "1.0")
//    new SwaggerHttp4s(docs.toYaml).routes
//  }
//}