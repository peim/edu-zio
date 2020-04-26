package com.peim

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, ExitCode, Resource, Timer}
import cats.implicits._
import com.peim.Main.{makeTransactor, readConfig}
import com.peim.config.Config
import com.peim.config.Config.DBConfig
import com.peim.dao.TagsRepo
import com.peim.domain.TagsService
import com.peim.domain.TagsService.TagsService
import com.peim.routes.TagsRoutes
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import fs2.Stream
import org.http4s.HttpApp
import org.http4s.client.Client
import org.http4s.implicits._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.blaze._
import org.http4s.server.middleware.{CORS, Logger}
import pureconfig.ConfigSource
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.putStrLn
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.{Implicits, global}

object Main extends App {

  type AppEnvironment = Blocking with Clock with TagsService

  type AppTask[A] = RIO[AppEnvironment, A]

  private def appLayer: ZIO[Blocking, Throwable, ZLayer[Any, Throwable, TagsService]] = {
    for {
      config <- readConfig
      dbConfig = config.dbConfig
      transactor <- makeTransactor(dbConfig)
    } yield {
      val transactorLayer = transactor.toLayer.orDie

      val tagsRepoLayer = transactorLayer >>> TagsRepo.live
      val tagsServiceLayer = tagsRepoLayer >>> TagsService.live
      Blocking.live >>> tagsServiceLayer
    }
  }


//
//  val server: ZIO[ZEnv, Throwable, Unit] = ZIO.runtime[ZEnv]
//    .flatMap {
//      implicit rts =>
//        for {
//          httpClient <- makeHttpClient
//          config <- readConfig
//
////          service <- ZIO.access[TagsService](_.get)
//          routes = new TagsRoutes()
//          httpApp = (
//            routes.swaggerDocsRoutes <+>
//              routes.getById <+>
//              routes.createTag
//            ).orNotFound
//
//          // With Middlewares in place
//          finalHttpApp = Logger.httpApp(true, true)(httpApp)
//
//          exitCode <- BlazeServerBuilder[Task]
//            .bindHttp(8080, "0.0.0.0")
//            .withHttpApp(finalHttpApp)
//            .serve
//            .compile[Task, Task, ExitCode]
//            .drain
//        } yield exitCode
//    }





  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    val program: ZIO[AppEnvironment, Throwable, Unit] =
      for {
        config <- readConfig
        apiConfig = config.appConfig
        dbConfig = config.dbConfig
        transactor <- makeTransactor(dbConfig)

        httpApp = Router[AppTask]("/users" -> TagsRoutes().routes).orNotFound

        server <- ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
          BlazeServerBuilder[AppTask]
            .bindHttp(apiConfig.port, apiConfig.host)
            .withHttpApp(CORS(httpApp))
            .serve
            .compile[AppTask, AppTask, ExitCode]
            .drain
        }
      } yield server

    val result = for {
      layer <- appLayer
      res <- program.provideSomeLayer[ZEnv](layer)
    } yield res

    result.foldM(
      err => putStrLn(s"Execution failed with: $err") *> IO.succeed(1),
      _ => IO.succeed(0)
    )
  }


  private def makeHttpServer(api: HttpApp[Task]) =
    ZIO
      .runtime[Any]
      .map { implicit rts =>
        BlazeServerBuilder[Task]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(api)
          .serve
          .compile[Task, Task, ExitCode]
          .drain
      }

  private def makeHttpClient: UIO[TaskManaged[Client[Task]]] =
    ZIO
      .runtime[Any]
      .map { implicit rts =>
        BlazeClientBuilder
          .apply[Task](Implicits.global)
          .resource
          .toManaged
      }

  private def makeTransactor(config: DBConfig): RIO[Blocking, RManaged[Blocking, Transactor[Task]]] = {
    def transactor(connectEC: ExecutionContext, transactEC: ExecutionContext): Resource[Task, Transactor[Task]] =
      HikariTransactor.newHikariTransactor[Task](
        config.driver,
        config.url,
        config.user,
        config.password,
        connectEC,
        Blocker.liftExecutionContext(transactEC)
      )

    ZIO
      .runtime[Blocking]
      .map { implicit rt =>
        for {
          transactEC <- ZIO.access[Blocking](_.get.blockingExecutor.asEC).toManaged_
          transactor <- transactor(rt.platform.executor.asEC, transactEC).toManaged
        } yield transactor
      }
  }

  private def readConfig: IO[Exception, Config] = {
    import pureconfig.generic.auto._
    ZIO
      .fromEither(ConfigSource.default.load[Config])
      .mapError(failures => new Exception(failures.prettyPrint()))
  }

}