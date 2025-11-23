package com.github.frederikpietzko.examples.graph

import com.github.frederikpietzko.domain.visitors.Visitor
import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.layout.CommonPage
import com.github.frederikpietzko.respondBaseTemplate
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.unsafe
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.toHTML
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layers.pie
import org.jetbrains.kotlinx.kandy.letsplot.style.Style
import org.jetbrains.kotlinx.kandy.util.color.Color

fun Application.graph() {
  routing {
    get("/graph") {
      val visitors = VisitorManagement.allVisitors()
      val (bars, pie) = graphs(visitors)
      call.respondBaseTemplate(CommonPage("Graph Showcase")) {
        children {
          div("flex flex-col p-4 gap-3") {
            id = "graph"
            unsafe { +bars }
            unsafe { +pie }
          }
        }
      }
    }
  }
}

private fun graphs(visitors: List<Visitor>): Pair<String, String> {
  val categoies = listOf(
    "like Javascipt and are wrong",
    "knows Kotlin",
    "knows Java",
    "knows HTMX",
  )
  val counts = listOf(
    visitors.count { !it.dislikesJavascript },
    visitors.count { it.knowsKotlin },
    visitors.count { it.knowsJava },
    visitors.count { it.knowsHtmx },
  )
  val bars = plot {
    layout {
      title = "Visitor Skills Overview"
    }
    bars {
      x(categoies, "category")
      y(counts, "count")
    }
  }.toHTML()
  val pie = dataFrameOf(
    "category" to categoies,
    "counts" to counts,
  ).plot {
    pie {
      slice("counts")
      fillColor("category")
      size = 20.0
      stroke = 1.0
      hole = 0.5
      strokeColor = Color.BLACK
    }
    layout {
      title = "Pie Chart of Visitor Skills"
      style(Style.Void)
    }
  }.toHTML()
  return Pair(bars, pie)
}
