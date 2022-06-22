package ru.itis.twitter.domain.twits.routes

import cats.effect.Concurrent
import cats.implicits.toFlatMapOps
import cats.{Monad, MonadError}
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import org.http4s.{AuthedRoutes, EntityDecoder, EntityEncoder, HttpRoutes, Request, Response}
import ru.itis.twitter.domain.twits.TwitAlgebra
import ru.itis.twitter.domain.users.User

class TwitsEndpoints[F[_] : TwitAlgebra : Concurrent] extends Http4sDsl[F] {
  def twitsEndpoints(): AuthedRoutes[User, F] = AuthedRoutes.of[User, F] {
    case req@POST -> Root / "twit" / "create" as user =>
      req.req
        .as[CreateTwitForm]
        .flatMap(value => TwitAlgebra[F].create(user, value.content))
        .flatMap(_ => Ok())

    case GET -> Root / "twits" / twitId as user =>
      TwitAlgebra[F].getTwitById(twitId.toLong)
        .flatMap {
          case Some(value) => Ok(value)
          case None => NotFound()
        }

    case DELETE -> Root / "twits" / twitId as user =>
      TwitAlgebra[F].delete(user, twitId.toLong).flatMap(_ => Ok())

    case GET -> Root / "user" / "twits" / userId as user =>
      TwitAlgebra[F].getUserTwits(userId.toLong).flatMap(_ => Ok())

    case GET -> Root / "feed" as user =>
      TwitAlgebra[F].getFeed(user.id).flatMap(Ok(_))

    case GET -> Root / "subscribe" / userId as user =>
      TwitAlgebra[F].subscribe(user.id, userId.toLong).flatMap(_ => Ok())

    case GET -> Root / "unsubscribe" / userId as user =>
      TwitAlgebra[F].unsubscribe(user.id, userId.toLong).flatMap(_ => Ok())
  }

  implicit val addTwitFormDecoder: Decoder[CreateTwitForm] = deriveDecoder

  implicit val addTwitFormEntityEncoder: EntityDecoder[F, CreateTwitForm] = jsonOf

  case class CreateTwitForm(content: String)
}
