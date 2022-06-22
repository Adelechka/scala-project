package ru.itis.twitter.auth

import java.util.UUID

case class Password(login: String, password: String)
case class AccessToken(token: UUID)
