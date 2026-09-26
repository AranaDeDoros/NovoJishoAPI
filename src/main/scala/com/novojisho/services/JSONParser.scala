package com.novojisho.services

import com.novojisho.domain.Jisho.*
import ujson.*
import util.Try

object JSONParser:

  case class ParsingError(message: String) extends Exception(message)
  private val RESULTS_LIMIT : Int = sys.env.get("JISHO_RESULTS_LIMIT")
                                      .flatMap(_.toIntOption).getOrElse(100)

  /**
   * default mapper function to transform raw json to a  Word
   */
  private val defaultMapperFunc: Value => Word = data =>
    val entry = data

    val japaneseWords =
      entry("japanese").arr.map { w =>
        val word =
          w.obj.get("word").map(_.str)

        val reading =
          w("reading").str

        JapaneseWord(word, reading)
      }.toVector

    val senses: Vector[Value] =
      entry.obj
        .get("senses")
        .map(_.arr.toVector)
        .getOrElse(Vector.empty)

    val englishDefinitions =
      senses.flatMap { sense =>
        sense.obj
          .get("english_definitions")
          .map(_.arr.map(_.str).toVector)
          .getOrElse(Vector.empty[String])
      }

    val speech =
      senses.flatMap { sense =>
        sense.obj
          .get("parts_of_speech")
          .map(_.arr.map(_.str).toVector)
          .getOrElse(Vector.empty[String])
      }

    Word(
      japaneseWords,
      EnglishDefinitions(englishDefinitions),
      SpeechParts(speech)
    )

  /**
   * Parses raw json into an Either while limiting results
   * up to the number defined in the RESULTS_LIMIT constant
   * @param raw raw json
   * @param f   mapper function to apply, set one by default
   * @return    Either of, Left for Parsing Error, Right a Vector of Word
   */
  def parse(
             raw: String,
             f: Value => Word = defaultMapperFunc
           ): Either[ParsingError, Vector[Word]] =
    if raw.isBlank then
      Left(ParsingError("Empty body"))
    else
      Try {
        val json = ujson.read(raw)
        val status = json("meta")("status").num.toInt
        (status, json("data"))
      }.toEither match
        case Left(e) =>
          Left(ParsingError(s"Failed to parse Jisho response: ${e.getMessage}"))
        case Right((status, dataJson)) if status != 200 =>
          Left(ParsingError(s"Jisho API returned status $status"))
        case Right((_, dataJson)) =>
          val data = dataJson.arr
          if data.isEmpty then Left(ParsingError("No results found"))
          else Right(data.map(f).toVector.take(RESULTS_LIMIT))