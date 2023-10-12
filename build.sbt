//Scala, Scalafmt, http4s, Cats Effect, fs2.Stream, fs2 kafka
ThisBuild / scalaVersion := "2.13.12"

val eventStore = (project in file ("./event-store"))
  .settings(moduleName := "event-store")
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.http4sDsl,
      Dependencies.http4sBlazeClient,
      Dependencies.http4sBlazeServer,
      Dependencies.catsEffect,
      Dependencies.ciris,
      Dependencies.log,
      Dependencies.circeGenericExtras,
      Dependencies.http4sCirce,
      Dependencies.kafka,
      Dependencies.elasticSearchClient
    ) ++ Dependencies.circe
  )
