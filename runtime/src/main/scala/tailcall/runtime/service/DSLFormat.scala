package tailcall.runtime.service

import caliban.parsing.Parser
import tailcall.runtime.dsl.json.Config
import tailcall.runtime.transcoder.Transcoder
import zio.json._
import zio.json.yaml._
import zio.{IO, ZIO}

sealed trait DSLFormat {
  self =>

  def ext: String =
    self match {
      case DSLFormat.JSON    => "json"
      case DSLFormat.YML     => "yml"
      case DSLFormat.GRAPHQL => "graphql"
    }

  def encode(config: Config): IO[String, String] =
    self match {
      case DSLFormat.JSON    => ZIO.succeed(config.toJsonPretty)
      case DSLFormat.YML     => ZIO.fromEither(config.toYaml(YamlOptions.default.copy(sequenceIndentation = 0)))
      case DSLFormat.GRAPHQL => Transcoder.toGraphQLSchema(config).toZIO
    }

  def decode(string: String): IO[String, Config] =
    self match {
      case DSLFormat.JSON    => ZIO.fromEither(string.fromJson[Config])
      case DSLFormat.YML     => ZIO.fromEither(string.fromYaml[Config])
      case DSLFormat.GRAPHQL => Parser.parseQuery(string).mapError(_.toString()).flatMap(Transcoder.toConfig(_).toZIO)
    }

  def endsWith(file: String): Boolean = file.endsWith(s".${ext}")
}

object DSLFormat {
  case object JSON    extends DSLFormat
  case object YML     extends DSLFormat
  case object GRAPHQL extends DSLFormat

  def formats: List[DSLFormat] = List(JSON, YML, GRAPHQL)

  def detect(name: String): IO[String, DSLFormat] =
    formats.find(_.endsWith(name)).fold[IO[String, DSLFormat]](ZIO.fail(s"Unsupported file: ${name}"))(ZIO.succeed(_))
}