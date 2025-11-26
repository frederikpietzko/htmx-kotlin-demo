package com.github.frederikpietzko.examples.realtime

import com.github.frederikpietzko.conferenceName
import com.github.frederikpietzko.insertBase
import com.github.frederikpietzko.layout.BaseTemplate
import com.github.frederikpietzko.layout.CommonPage
import com.github.frederikpietzko.wsConnect
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.id

class VisitorStatisticPage(
  private val model: VisitorStatistic,
) : BaseTemplate<HTML>() {
  context(application: Application)
  override fun HTML.render() {
    insertBase(CommonPage("Realtime Visitor Statistics")) {
      children {
        div {
          attributes.hx {
            ext = "ws"
            wsConnect = "/realtime/ws"
          }
          insertBase(Statistics(model))
        }
      }
    }
  }
}

class Statistics(
  private val statistic: VisitorStatistic,
) : BaseTemplate<FlowContent>() {

  context(application: Application)
  override fun FlowContent.render() {
    div("stat shadow grid grid-cols-4") {
      id = "stats"
      stat("${application.conferenceName} Visitors", statistic.totalVisitors, "Total visitors")
      stat("Average Age", statistic.averageAge, "Average age of visitors")
      stat("Know Kotlin", statistic.knowsKotlin, "Visitors who know Kotlin", "text-secondary")
      stat("Know Java", statistic.knowsJava, "Visitors who know Java")
      stat("Knows HTMX", statistic.knowsHtmx, "Visitors who know HTMX")
      stat("Dislike JavaScript", statistic.dislikesJavascript, "Totally relatable people", "text-success")
      stat("Like JavaScript", statistic.likesJavascript, "People who are wrong", "text-error")
    }
  }
}

private fun FlowContent.stat(title: String, value: Int, desc: String, highlight: String = "text-primary") =
  div("stat") {
    div("stat-title") {
      +title
    }
    div("stat-value $highlight") {
      +value.toString()
    }
    div("stat-desc") {
      +desc
    }
  }
