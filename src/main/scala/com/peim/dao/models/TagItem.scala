package com.peim.dao.models

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class TagItem(id: String, key: String, value: String)

object TagItem {
  implicit val jokeDecoder: Decoder[TagItem] = deriveDecoder[TagItem]
  implicit def jokeEntityDecoder[F[_]: Sync]: EntityDecoder[F, TagItem] =
    jsonOf
  implicit val jokeEncoder: Encoder[TagItem] = deriveEncoder[TagItem]
  implicit def jokeEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TagItem] =
    jsonEncoderOf
}