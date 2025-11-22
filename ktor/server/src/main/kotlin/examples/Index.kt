package com.github.frederikpietzko.examples

import com.github.frederikpietzko.conferenceName
import com.github.frederikpietzko.conferenceYear
import com.github.frederikpietzko.layout.CommonPage
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p

fun Application.index() {
  routing {
    get("/") {
      call.respondHtmlTemplate(CommonPage("$conferenceName HTMX Demo")) {
        children {
          div("hero bg-base-200 m-auto") {
            div("hero-content text-center") {
              div("max-w-lg") {
                h1("text-5xl font-bold") {
                  +" Hello $conferenceName $conferenceYear!"
                }
                p("p-6") {
                  +"This is a demo showing the power of kotlinx.html and HTMX!"
                }
                a(href = "/form", classes = "btn btn-primary") {
                  +"Get started"
                }
              }
            }
          }
        }
      }
    }
  }
}
