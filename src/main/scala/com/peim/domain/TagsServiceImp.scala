package com.peim.domain

import cats.implicits._
import com.peim.dao.TagsRepo
import com.peim.dao.models.TagItem
import com.peim.domain.TagsService.Service
import zio.Task

private[domain] final case class TagsServiceImp(tagsRepo: TagsRepo.Service) extends Service {
//  override def getById(id: String): Task[Either[String, TagItem]] =
//    tagsRepo.getById(id).map {
//      case Some(v) => v.asRight[String]
//      case None => s"Tag by id $id not found".asLeft[TagItem]
//    }

  override def create(tag: TagItem): Task[Either[String, Unit]] =
    tagsRepo.create(tag).map(_ => ().asRight[String])

  override def getById(id: String): Task[TagItem] =
    tagsRepo.getById(id).map(_.getOrElse(TagItem("1","2","3")))
}
