package ru.itis.twitter.domain.users

trait UserValidator {
  def validateLogin(user: User): Either[UserValidateError, User]
  def validatePassword(user: User, password: String): Either[UserValidateError, User]
}

class UserValidatorImpl extends UserValidator {

  def validateLogin(user: User): Either[UserValidateError, User] = user.login match {
    case value if value.isEmpty => Left(ShortLogin())
    case value if value.length < 5 => Left(ShortLogin())
    case _ => Right(user)
  }

  def validatePassword(user: User, password: String): Either[UserValidateError, User] = password match {
    case value if value.length < 6 => Left(ShortPassword())
    case _ => Right(user)
  }
}

class UserValidateError(val message: String) extends Throwable(message)

case class ShortLogin() extends UserValidateError("login should contain at least 5 symbols")
case class ShortPassword() extends UserValidateError("password should contain at least 6 symbols")
case class LoginOccupied() extends UserValidateError("login is already exist")
