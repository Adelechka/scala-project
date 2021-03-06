package ru.itis.twitter

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

package object config {
  implicit val databaseConfigDecoder: Decoder[DatabaseConfig] = deriveDecoder
  implicit val httpConfigDecoder: Decoder[HttpConfig] = deriveDecoder
  implicit val twitterConfigDecoder: Decoder[TwitterConfig] = deriveDecoder
}
