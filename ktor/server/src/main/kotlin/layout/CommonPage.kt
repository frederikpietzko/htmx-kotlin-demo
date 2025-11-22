package com.github.frederikpietzko.layout

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML

class CommonPage(val title: String) : Template<HTML> {
  val children = TemplatePlaceholder<FlowContent>()

  override fun HTML.apply() {
    insert(Page(this@CommonPage.title)) {
      content {
        navbar {
          menuItems = listOf(
            Navbar.MenuItem(
              "Form",
              children = listOf(
                Navbar.MenuItem("Basic Form", "/form"),
                Navbar.MenuItem("Server Side Validation", "/form/server-validation"),
              ),
            ),
            Navbar.MenuItem("Table", "/table"),
            Navbar.MenuItem("Realtime", "/realtime"),
          )
        }
      }
    }
  }
}
