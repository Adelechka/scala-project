package ru.itis.twitter.domain.twits.repositories

import ru.itis.twitter.domain.twits.Twit
import ru.itis.twitter.domain.users.User

trait TwitsRepositoryAlgebra[F[_]] {
  def save(twit: Twit, user: User): F[Unit]
  def getById(twitId: Long): F[Option[Twit]]
  def delete(twitId: Long): F[Unit]
  def getUserTwits(userId: Long): F[List[Twit]]
  def getFeed(userId: Long): F[List[Twit]]
  def subscribe(subscriberId: Long, userId: Long): F[Unit]
  def unsubscribe(subscriberId: Long, userId: Long): F[Unit]
}

object TwitsRepositoryAlgebra {
  def apply[F[_]](implicit twitRepository: TwitsRepositoryAlgebra[F]): TwitsRepositoryAlgebra[F] = twitRepository
}
