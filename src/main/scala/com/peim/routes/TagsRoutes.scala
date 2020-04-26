package com.peim.routes

import com.peim.dao.models.TagItem
import com.peim.domain.TagsService
import com.peim.domain.TagsService.TagsService
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import sttp.tapir.server.http4s._
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import zio.{RIO, Task, ZIO}
import zio.interop.catz._
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import zio._
import org.http4s.circe._
import zio.interop.catz._
import io.circe.generic.auto._

final case class TagsRoutes[R <: TagsService]() {

  type UserTask[A] = RIO[R, A]

  implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[UserTask, A] = jsonOf[UserTask, A]
  implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[UserTask, A] = jsonEncoderOf[UserTask, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]
  import dsl._

  def routes: HttpRoutes[UserTask] = {
    HttpRoutes.of[UserTask] {
      case GET -> Root / "tags" / IntVar(id) =>
        TagsService.getById(id.toString).foldM(_ => NotFound(), Ok(_))
//      case request @ POST -> Root =>
//        request.decode[User] { user =>
//          Created(createUser(user))
//        }
//      case DELETE -> Root / IntVar(id) =>
//        (getUser(id) *> deleteUser(id)).foldM(_ => NotFound(), Ok(_))
    }
  }

//  def getById(id: String) = {
//    Endpoints.getTagById.toRoutes(id => tagsService.getById(id))
//  }
//
//  def createTag(tag: TagItem) = {
//    Endpoints.createTag.toRoutes(tag => tagsService.create(tag))
//  }
}