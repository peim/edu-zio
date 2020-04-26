package com.peim.config

import com.peim.config.Config.{AppConfig, DBConfig}

final case class Config(appConfig: AppConfig, dbConfig: DBConfig)

object Config {
  final case class AppConfig(host: String, port: Int)
  final case class DBConfig(url: String, driver: String, user: String, password: String)
}
