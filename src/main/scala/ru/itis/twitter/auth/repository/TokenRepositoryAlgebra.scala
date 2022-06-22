package ru.itis.twitter.auth.repository

import cats.data.OptionT
import ru.itis.twitter.auth.AccessToken
import ru.itis.twitter.domain.users.User

trait TokenRepositoryAlgebra[F[_]] {
  def createToken(user: User): F[AccessToken]
  def getUserByToken(token: AccessToken): OptionT[F, User]
}

object TokenRepositoryAlgebra {
  def apply[F[_]](implicit tokenRepository: TokenRepositoryAlgebra[F]): TokenRepositoryAlgebra[F] = tokenRepository
}