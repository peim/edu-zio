package com.peim.dao

import com.peim.dao.models.TagItem
import doobie.util.transactor.Transactor
import zio.{Has, Task, ZLayer}

object TagsRepo {
  type TagsRepo = Has[Service]

  trait Service {
    def getAll: Task[Set[TagItem]]

    def getById(id: String): Task[Option[TagItem]]

    def delete(id: String): Task[Unit]

    def create(tag: TagItem): Task[Int]

    def update(id: String, tag: TagItem): Task[Int]
  }

  val live: ZLayer[Has[Transactor[Task]], Throwable, Has[Service]] =
    ZLayer.fromService[Transactor[Task], Service] { xa: Transactor[Task] =>
      TagsRepoImpl(xa)
    }
}
