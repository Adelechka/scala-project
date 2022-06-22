package ru.itis.twitter.domain.users

import ru.itis.twitter.domain.users.repositories.UserRepositoryAlgebra

trait UserAlgebra[F[_]] {
  def saveUser(user: User): F[Unit]
}

object UserAlgebra {
  def apply[F[_]](implicit userAlgebra: UserAlgebra[F]): UserAlgebra[F] = userAlgebra
  implicit def defaultUserAlgebra[F[_]](implicit userRepository: UserRepositoryAlgebra[F]): UserAlgebra[F] = (user: User) => userRepository.save(user)
}
