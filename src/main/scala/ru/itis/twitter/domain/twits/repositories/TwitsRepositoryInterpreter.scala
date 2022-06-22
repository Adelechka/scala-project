package ru.itis.twitter.domain.twits.repositories

import cats.Functor
import cats.effect.MonadCancelThrow
import cats.syntax.functor._
import doobie.implicits._
import doobie.util.transactor.Transactor
import ru.itis.twitter.domain.twits.Twit
import ru.itis.twitter.domain.users.User

class TwitsRepositoryInterpreter[F[_] : MonadCancelThrow : Functor](val transactor: Transactor[F]) extends TwitsRepositoryAlgebra[F] {

  def insert(content: String, ownerId: Long): doobie.Update0 =
    sql"INSERT INTO twit (content, twit_owner) VALUES($content, $ownerId)".update

  def selectById(twitId: Long): doobie.Query0[Twit] =
    sql"SELECT * FROM twit WHERE id=$twitId".query[Twit]

  def selectByOwnerId(ownerId: Long): doobie.Query0[Twit] =
    sql"SELECT * FROM twit WHERE twit_owner=$ownerId".query[Twit]

  def deleteById(twitId: Long): doobie.Update0  =
    sql"DELETE FROM twit WHERE id=$twitId".update

  def selectFeed(userId: Long) =
    (sql"WITH subscribes AS(SELECT user_id FROM subscribes WHERE subscriber_id=$userId) " ++
      sql"SELECT * FROM twit WHERE twit_owner IN (SELECT * FROM subscribes)").query[Twit]

  def insertSubscribe(subscriberId: Long, userId: Long): doobie.Update0 =
    sql"INSERT INTO subscribes(subscriber_id, user_id) VALUES ($subscriberId, $userId)".update

  def deleteSubscribe(subscriberId: Long, userId: Long): doobie.Update0 =
    sql"DELETE FROM subscribes WHERE subscriber_id=$subscriberId AND user_id=$userId".update

  override def save(twit: Twit, user: User): F[Unit] =
    insert(twit.content, user.id).run.transact(transactor).as(())

  override def getById(twitId: Long): F[Option[Twit]] =
    selectById(twitId).option.transact(transactor)

  override def delete(twitId: Long): F[Unit] =
    deleteById(twitId).run.transact(transactor).as(())

  override def getUserTwits(userId: Long): F[List[Twit]] =
    selectByOwnerId(userId).to[List].transact(transactor)

  override def getFeed(userId: Long): F[List[Twit]] =
    selectFeed(userId).to[List].transact(transactor)

  override def subscribe(subscriberId: Long, userId: Long): F[Unit] =
    insertSubscribe(subscriberId, userId).run.transact(transactor).as(())

  override def unsubscribe(subscriberId: Long, userId: Long): F[Unit] =
    deleteSubscribe(subscriberId, userId).run.transact(transactor).as(())
}
