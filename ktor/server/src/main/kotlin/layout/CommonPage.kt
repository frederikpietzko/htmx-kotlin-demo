package com.github.frederikpietzko.layout

import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML

class CommonPage(val title: String) : BaseTemplate<HTML>() {
  val children = Placeholder<FlowContent>()

  context(application: Application)
  override fun HTML.render() {
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
        content {
          insert(children)
        }
      }
    }
  }
}
