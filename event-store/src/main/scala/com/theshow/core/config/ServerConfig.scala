package com.theshow.core.config
import cats.effect.kernel.Async
import com.theshow.core.domain.{Host,Port}
import ciris.{ConfigValue, env}
import cats.implicits._


final case class ServerConfig(
   port: Port,
   host: Host
)

object ServerConfig {
  def serverConfig[F[_]: Async]: ConfigValue[F, ServerConfig] = (
    env("Port").as[Int].default(8080),
    env("Port").default("localhost")
  ).parMapN((port, host) => ServerConfig(Port(port), Host(host)))
}
