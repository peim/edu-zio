package com.peim.dao

import com.peim.dao.TagsRepo.Service
import com.peim.dao.models.TagItem
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.{Query0, Update0}
import zio.Task
import zio.interop.catz._

private[dao] final case class TagsRepoImpl(xa: Transactor[Task]) extends Service {

  override def getAll: Task[Set[TagItem]] =
    SQL.getAll
      .to[Set]
      .transact(xa)

  override def getById(id: String): Task[Option[TagItem]] =
    SQL
      .get(id)
      .option
      .transact(xa)

  override def delete(id: String): Task[Unit] =
    SQL
      .delete(id)
      .run
      .transact(xa)
      .unit

  override def create(tag: TagItem): Task[Int] =
    SQL
      .create(tag)
      .run
      .transact(xa)

  override def update(id: String, tag: TagItem): Task[Int] =
    SQL.update(tag)
      .run
      .transact(xa)
}

private object SQL{
    def create(tag: TagItem): Update0 = sql"""
      INSERT INTO tags (id, `key`, `value`)
      VALUES (${tag.id}, ${tag.key}, ${tag.value})
      """.update

    def get(id: String): Query0[TagItem] = sql"""
      SELECT * FROM tags WHERE id = $id
      """.query[TagItem]

    val getAll: Query0[TagItem] = sql"""
      SELECT * FROM tags
      """.query[TagItem]

    def delete(id: String): Update0 = sql"""
      DELETE from tags WHERE id = $id
      """.update

    def update(tag: TagItem): Update0 = sql"""
      UPDATE tags SET
      `key` = ${tag.key},
      `value` = ${tag.value}
      WHERE id = ${tag.id}
      """.update
  }
