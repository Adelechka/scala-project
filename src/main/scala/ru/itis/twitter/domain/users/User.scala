package ru.itis.twitter.domain.users


case class User(id: Long, login: String, passwordHash: String)

