package com.github.frederikpietzko.examples.form

import com.github.frederikpietzko.domain.visitors.Visitor
import kotlinx.serialization.Serializable

@Serializable
class RegisterFormSubmission(
  val name: String = "",
  val age: Int = 0,
  val knowsKotlin: Boolean = false,
  val knowsJava: Boolean = false,
  val knowsHtmx: Boolean = false,
  val dislikesJavascript: Boolean = false,
) {

  fun toVisitor() = Visitor(
    name,
    age.toInt(),
    knowsKotlin,
    knowsJava,
    knowsHtmx,
    dislikesJavascript
  )
}
