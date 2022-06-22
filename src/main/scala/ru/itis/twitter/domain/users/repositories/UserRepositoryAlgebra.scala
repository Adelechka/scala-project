package ru.itis.twitter.domain.users.repositories

import cats.data.OptionT
import ru.itis.twitter.domain.users.User

trait UserRepositoryAlgebra[F[_]] {
  def save(user: User): F[Unit]
  def getById(id: Long): OptionT[F, User]
  def getAll: F[List[User]]
  def getByLogin(login: String): OptionT[F, User]
}

object UserRepositoryAlgebra {
  def apply[F[_]](implicit userRepository: UserRepositoryAlgebra[F]): UserRepositoryAlgebra[F] = userRepository
}
