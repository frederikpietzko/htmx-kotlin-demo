package com.github.frederikpietzko.layout

import io.ktor.server.html.*
import kotlinx.html.BODY
import kotlinx.html.FlowContent

class Layout : Template<BODY> {
  val navbar = TemplatePlaceholder<Navbar>()
  val content = Placeholder<FlowContent>()

  override fun BODY.apply() {
    insert(Navbar(), navbar)
    insert(content)
  }
}
