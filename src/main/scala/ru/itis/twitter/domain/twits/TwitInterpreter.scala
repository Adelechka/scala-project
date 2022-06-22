package ru.itis.twitter.domain.twits

import cats.Functor
import ru.itis.twitter.domain.twits.repositories.TwitsRepositoryAlgebra
import ru.itis.twitter.domain.users.User

class TwitInterpreter[F[_] : TwitsRepositoryAlgebra : Functor] extends TwitAlgebra[F] {

  override def create(user: User, content: String): F[Unit] =
    TwitsRepositoryAlgebra[F].save(Twit(0, content, user.id), user)

  override def delete(user: User, twitId: Long): F[Unit] =
    TwitsRepositoryAlgebra[F].delete(twitId)

  override def getUserTwits(userId: Long): F[List[Twit]] =
    TwitsRepositoryAlgebra[F].getUserTwits(userId)

  override def getFeed(userId: Long): F[List[Twit]] =
    TwitsRepositoryAlgebra[F].getFeed(userId)

  override def subscribe(subscriberId: Long, userId: Long): F[Unit] =
    TwitsRepositoryAlgebra[F].subscribe(subscriberId, userId)

  override def unsubscribe(subscriberId: Long, userId: Long): F[Unit] =
    TwitsRepositoryAlgebra[F].unsubscribe(subscriberId, userId)

  override def getTwitById(twitId: Long): F[Option[Twit]] =
    TwitsRepositoryAlgebra[F].getById(twitId)
}
