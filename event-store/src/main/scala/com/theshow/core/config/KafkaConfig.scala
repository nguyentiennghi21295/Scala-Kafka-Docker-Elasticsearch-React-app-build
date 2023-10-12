package com.theshow.core.config

import cats.effect.kernel.Async
import ciris.{ConfigValue, env}
import cats.implicits._
import com.theshow.core.domain.{BootStrapServer, GroupId, Topic}

case class KafkaConfig (bootstrapServer: BootStrapServer, groupId: GroupId, topic: Topic)

object KafkaConfig{
  def kafkaConfig[F[_]: Async]: ConfigValue[F, KafkaConfig] = (
    env("BOOSTRAP_SERVER").as[String].default("localhost:9092"),
    env("GROUP_ID").as[String].default("consumerGroup"),
    env("KAFKA_TOPIC").as[String].default("thoughtsTopic")
  ).parMapN{ (port, groupId, topic) =>
    KafkaConfig(BootStrapServer(port), GroupId(groupId), Topic(topic))
  }
}