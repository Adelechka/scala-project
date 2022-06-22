package ru.itis.twitter.builders

import cats.Functor
import cats.effect._
import cats.implicits.toSemigroupKOps
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server}
import ru.itis.twitter.config.TwitterConfig
import ru.itis.twitter.domain.twits.repositories._
import ru.itis.twitter.domain.twits.routes.TwitsEndpoints
import ru.itis.twitter.domain.twits.{TwitAlgebra, TwitInterpreter}
import ru.itis.twitter.auth.repository.TokenRepositoryInterpreter
import ru.itis.twitter.auth.routes.SecurityEndpoints
import ru.itis.twitter.auth.{AccessTokenSecurityInterpreter, Middleware, PasswordSecurityInterpreter}
import ru.itis.twitter.domain.users.repositories.{UserRepositoryAlgebra, UserRepositoryInterpreter}
import ru.itis.twitter.domain.users.UserValidatorImpl

class ServerBuilder[F[_]: Async : Sync : Functor] {

  def buildServer: Resource[F, Server] =
    for {
      conf <- TwitterConfig.getConfig
      _ <- Resource.eval(DatabaseServicesBuilder.migrateDatabase(conf.db))
      transactor <- DatabaseServicesBuilder.transactor[F](conf.db)
      implicit0(userRepository: UserRepositoryAlgebra[F]) = new UserRepositoryInterpreter[F](transactor)
      implicit0(tokenRepository: TokenRepositoryInterpreter[F]) = new TokenRepositoryInterpreter[F](transactor)
      implicit0(passwordSecurityAlgebra: PasswordSecurityInterpreter[F]) = new PasswordSecurityInterpreter[F]
      implicit0(tokenSecurityAlgebra: AccessTokenSecurityInterpreter[F]) = new AccessTokenSecurityInterpreter[F]
      securityEndpoints = new SecurityEndpoints[F](new UserValidatorImpl)
      implicit0(nra: TwitsRepositoryAlgebra[F]) = new TwitsRepositoryInterpreter[F](transactor)
      implicit0(na: TwitAlgebra[F]) = new TwitInterpreter[F]
      authMiddleware = new Middleware[F].authMiddleware
      twitsEndpoints = new TwitsEndpoints[F]
      httpApp = Router(
        "/" -> (securityEndpoints.getEndpoints <+> authMiddleware(twitsEndpoints.twitsEndpoints()))
      ).orNotFound
      server <- BlazeServerBuilder[F]
        .bindHttp(port = conf.http.port)
        .withHttpApp(httpApp)
        .resource
    } yield server


}
