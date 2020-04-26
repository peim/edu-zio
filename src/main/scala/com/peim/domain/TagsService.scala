package com.peim.domain

import com.peim.dao.TagsRepo.TagsRepo
import com.peim.dao.TagsRepo
import com.peim.dao.models.TagItem
import zio.{Has, RIO, Task, ZLayer}

object TagsService {
  type TagsService = Has[Service]

  trait Service {
    def getById(id: String): Task[TagItem]

    def create(tag: TagItem): Task[Either[String, Unit]]
  }

  def getById(id: String): RIO[TagsService, TagItem] = RIO.accessM(_.get.getById(id))

  def live: ZLayer[TagsRepo, Throwable, Has[Service]] =
    ZLayer.fromService[TagsRepo.Service, Service] { tagsRepo =>
        TagsServiceImp(tagsRepo)
    }
}
