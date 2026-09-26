package com.novojisho.infra


case class ApiError(message: String)

object ApiError:
  def badRequest(msg: String) = ApiError(msg)
  def notFound(msg: String)   = ApiError(msg)
  def upstream(msg: String)   = ApiError(msg)

object DTO :
  case class Page(number: Int) extends AnyVal
  case class Term(lookup: String) extends AnyVal:
    override def toString: String = lookup