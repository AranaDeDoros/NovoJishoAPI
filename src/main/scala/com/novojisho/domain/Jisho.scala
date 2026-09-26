package com.novojisho.domain

object Jisho:

  case class JapaneseWord(word: Option[String], reading: String)
  case class EnglishDefinitions(definitions: Seq[String]):
    override def toString: String = this.definitions.mkString(",")
  case class SpeechParts(parts: Seq[String]):
    override def toString: String = this.parts.mkString(",")
  case class Word(japanese : Vector[JapaneseWord], english: EnglishDefinitions, speech: SpeechParts)

