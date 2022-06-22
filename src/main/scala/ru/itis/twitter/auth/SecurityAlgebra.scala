package ru.itis.twitter.auth

import cats.data.EitherT
import ru.itis.twitter.domain.users.User


trait SecurityAlgebra[F[_], Cred] {
  def auth(cred: Cred): EitherT[F, SecurityError, User]
  def createCred(user: User): EitherT[F, SecurityError, Cred]
}

object SecurityAlgebra {
  def apply[F[_], Cred](implicit securityAlgebra: SecurityAlgebra[F, Cred]): SecurityAlgebra[F, Cred] = securityAlgebra
}
