package ru.itis.twitter.auth

class SecurityError(val message: String) extends RuntimeException(message)

class PasswordCreationNotSupported extends SecurityError("password creation not supported")
class CredNotValid extends SecurityError("wrong cred")
class UserNotFound extends SecurityError("user not found")
