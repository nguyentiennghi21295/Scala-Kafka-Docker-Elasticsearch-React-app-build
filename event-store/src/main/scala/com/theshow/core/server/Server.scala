package com.theshow.core.server
import com.theshow.core.config._
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import cats.effect.std.Console
import com.theshow.core.elasticsearch.EsAlgebra
import com.theshow.core.http.routes.EventRoutes
import org.http4s.blaze.server._
import fs2.Stream
import com.theshow.core.kafka.{KafkaConsumerAlgebra, KafkaProducerAlgebra}
import com.theshow.core.service.IndexService
import org.elasticsearch.client.RestHighLevelClient
import org.http4s.server.middleware.{CORS, CORSConfig}



object Server {
  def stream[F[_]: Async: Console](
          config: Config,
          restHighLevelClient: RestHighLevelClient
          ): fs2.Stream[F, ExitCode] = for {
            _ <- Stream.eval(Console[F].println("Starting the server "))

              kafkaProducerAlgebra: KafkaProducerAlgebra[F] = KafkaProducerAlgebra
                .impl[F](config.kafkaConfig)
              kafkaConsumerAlgebra: KafkaConsumerAlgebra[F] =
                KafkaConsumerAlgebra.impl[F](config.kafkaConfig)
              esAlgebra      = EsAlgebra.impl[F](config.esConfig, restHighLevelClient)
              indexService = IndexService.impl[F](kafkaConsumerAlgebra, esAlgebra)

              _ <- Stream.eval(esAlgebra.createIndex)

          corService = CORS(EventRoutes[F](kafkaProducerAlgebra).router,
              CORSConfig.default.withAllowedOrigins(Set("https://localhost:3000"))
                .withAllowedMethods(Some(Set(Method.POST)))
            )


    server <- BlazeServerBuilder[F]
      .bindHttp(
        config.serverConfig.port.value,
        config.serverConfig.host.value
      )
      .withHttpApp(EventRoutes[F](kafkaProducerAlgebra).router.orNotFound)
      .serve
      .concurrently(indexService.persist)
  } yield server
}
