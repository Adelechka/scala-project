package ru.itis.twitter.auth

import cats.effect.kernel.Sync
import com.github.t3hnar.bcrypt._

trait PasswordEncoderAlgebra[F[_]] {
  def encode(password: String): F[String]
  def check(password: String, passwordHash: String): Boolean
}

object PasswordEncoderAlgebra {

  def apply[F[_]](implicit passwordEncoder: PasswordEncoderAlgebra[F]): PasswordEncoderAlgebra[F] = passwordEncoder

  implicit class PasswordEncoderOps(value: String) {
    def encode[F[_]](implicit passwordEncoder: PasswordEncoderAlgebra[F]): F[String] = passwordEncoder.encode(value)
    def check[F[_]](passwordHash: String)(implicit passwordEncoder: PasswordEncoderAlgebra[F]): Boolean = passwordEncoder.check(value, passwordHash)
  }

  implicit def defaultPasswordEncoder[F[_] : Sync]: PasswordEncoderAlgebra[F] = new PasswordEncoderAlgebra[F] {
    override def encode(password: String): F[String] = Sync[F].delay(password.boundedBcrypt)
    override def check(password: String, passwordHash: String): Boolean = password.isBcryptedBounded(passwordHash)
  }
}
