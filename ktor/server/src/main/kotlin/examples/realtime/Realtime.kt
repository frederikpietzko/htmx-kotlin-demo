package com.github.frederikpietzko.examples.realtime

import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.domain.visitors.VisitorRepository
import com.github.frederikpietzko.respondBaseTemplate
import com.github.frederikpietzko.sendHtml
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Application.realtime() {
  routing {
    get("/realtime") {
      val visitors = VisitorManagement.allVisitors()
      val statistics = VisitorStatistic.fromVisitors(visitors)
      call.respondBaseTemplate(VisitorStatisticPage(statistics))
    }
    webSocket("/realtime/ws") {
      VisitorRepository.visitors.collect { visitors ->
        sendHtml(Statistics(VisitorStatistic.fromVisitors(visitors)))
      }
    }
  }
}
