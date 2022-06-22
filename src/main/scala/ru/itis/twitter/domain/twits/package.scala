package ru.itis.twitter.domain

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

package object twits {
  implicit def twitEncoder: Encoder[Twit] = deriveEncoder
  implicit def twitListEncoder: Encoder[List[Twit]] = deriveEncoder
  implicit def twitEntityEncoder[F[_]]: EntityEncoder[F, Twit] = jsonEncoderOf
  implicit def twitListEntityEncoder[F[_]]: EntityEncoder[F, List[Twit]] = jsonEncoderOf

}
