package ru.itis.twitter.domain.twits

import cats.data.EitherT
import ru.itis.twitter.domain.users.User

trait TwitAlgebra[F[_]] {
  def create(user: User, content: String): F[Unit]
  def delete(user: User, twitId: Long): F[Unit]
  def getTwitById(twitId: Long): F[Option[Twit]]
  def getUserTwits(userId: Long): F[List[Twit]]
  def getFeed(userId: Long): F[List[Twit]]
  def subscribe(subscriberId: Long, userId: Long): F[Unit]
  def unsubscribe(subscriberId: Long, userId: Long): F[Unit]
}

object TwitAlgebra {
  def apply[F[_]](implicit notebookAlgebra: TwitAlgebra[F]): TwitAlgebra[F] = notebookAlgebra
}
