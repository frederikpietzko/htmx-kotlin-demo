package com.github.frederikpietzko.layout

import com.github.frederikpietzko.static
import io.ktor.htmx.html.*
import io.ktor.server.html.*
import kotlinx.html.*

class Page(
  val title: String,
) : Template<HTML> {
  val content = TemplatePlaceholder<Layout>()

  override fun HTML.apply() {
    head {
      title(this@Page.title)
      meta {
        name = "viewport"
        content = "width=device-width, initial-scale=1.0"
      }

      script {
        src = "htmx.min.js".static()
        defer = true
      }
      script {
        src = "htmx.ext.sse.min.js".static()
        defer = true
      }

      script {
        src = "htmx.ext.ws.min.js".static()
        defer = true
      }
      script {
        src = "hyperscript.min.js".static()
        defer = true
      }
      link(
        href = "fa-all.min.css".static(),
        rel = "stylesheet",
      )
      link(href = "daisyui.min.css".static(), rel = "stylesheet")
      script { src = "tailwind.min.js".static() }
    }
    body {
      attributes.hx {
        boost = true
        ext = "sse ws"
      }
      insert(Layout(), content)
    }
  }
}

