package com.novojisho

import com.novojisho.infra.DTO.Term


/**
 * string interpolator for queries
 */
extension (sc: StringContext)
  def t(args: Any*): Term =
    Term(sc.s(args*))