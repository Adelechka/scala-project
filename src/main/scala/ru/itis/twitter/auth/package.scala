package ru.itis.twitter

import cats.effect.kernel.Concurrent
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import ru.itis.twitter.auth.AccessToken

package object security {
  implicit def accessTokenDecoder: Decoder[AccessToken] = deriveDecoder

  implicit def accessTokenEncoder: Encoder[AccessToken] = deriveEncoder

  implicit def accessTokenEntityEncoder[F[_]]: EntityEncoder[F, AccessToken] = jsonEncoderOf
}
