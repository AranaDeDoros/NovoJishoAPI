package com.novojisho.services
import com.novojisho.infra.DTO.Term
import sttp.client4.*

object JishoClient:

  private val JISHO_URL : String = sys.env.getOrElse
        ("JISHO_URL", "https://jisho.org/api/v1/search/words")

  def searchForWord(term : Term): Either[String, String] =
    val searchURI = uri"$JISHO_URL".addParam("keyword", term.lookup)
    val response = basicRequest
      .get(searchURI)
      .send(DefaultSyncBackend())
    response.body
