package ru.itis.twitter.config

import cats.ApplicativeError
import cats.effect.Resource
import io.circe.config.parser

case class TwitterConfig(db: DatabaseConfig, http: HttpConfig)

object TwitterConfig {
  def getConfig[F[_]: ApplicativeError[*[_], Throwable]]: Resource[F, TwitterConfig] = {
    Resource.eval(parser.decodeF[F, TwitterConfig]())
  }
}
