package com.novojisho

import sttp.tapir.*
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import com.novojisho.infra.ApiError
import com.novojisho.infra.DTO.*
import com.novojisho.services.{JSONParser, JishoClient}
import sttp.shared.Identity
import sttp.tapir.generic.auto.*
import sttp.tapir.json.jsoniter.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.model.StatusCode
import domain.Jisho.*

object Endpoints:

  given wordCodec: JsonValueCodec[Vector[Word]] = JsonCodecMaker.make
  given PageCodec: JsonValueCodec[Vector[Page]] = JsonCodecMaker.make
  given TermCodec: JsonValueCodec[Vector[Term]] = JsonCodecMaker.make
  given apiErrorCodec: JsonValueCodec[ApiError] = JsonCodecMaker.make

  private val wordEndpoint: PublicEndpoint[Term, (StatusCode, ApiError), Vector[Word], Any] =
    endpoint.get
      .in("jisho")
      .in(query[Term]("term"))
      .errorOut(statusCode.and(jsonBody[ApiError]))
      .out(jsonBody[Vector[Word]])

  private val wordServerEndpoint: ServerEndpoint[Any, Identity] =
    wordEndpoint.handle: (term) =>
      JishoClient.searchForWord(t"$term") match
        case Left(err) =>
          Left((StatusCode.BadGateway, ApiError.upstream(err)))
        case Right(raw) =>
          JSONParser.parse(raw) match
            case Left(parsingErr) =>
              Left((StatusCode.UnprocessableEntity, ApiError.badRequest(parsingErr.message)))
            case Right(words) =>
              Right(words)

  // catch-all
  private val notFoundEndpoint: PublicEndpoint[List[String], Unit, ApiError, Any] =
    endpoint
      .in(paths) // consumes all remaining path segments, whatever they are
      .out(statusCode(StatusCode.NotFound))
      .out(jsonBody[ApiError])

  private val notFoundServerEndpoint: ServerEndpoint[Any, Identity] =
    notFoundEndpoint.handleSuccess: _ =>
      ApiError.notFound("The requested resource was not found")

  private val apiEndpoints: List[ServerEndpoint[Any, Identity]] = List(wordServerEndpoint)

  private val docEndpoints: List[ServerEndpoint[Any, Identity]] = SwaggerInterpreter()
    .fromServerEndpoints[Identity](apiEndpoints, "Jisho Novo API", "1.0.0")

  val all: List[ServerEndpoint[Any, Identity]] =
    apiEndpoints ++ docEndpoints ++ List(notFoundServerEndpoint)