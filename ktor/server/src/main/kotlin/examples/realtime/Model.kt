package com.github.frederikpietzko.examples.realtime

import com.github.frederikpietzko.domain.visitors.Visitor

data class VisitorStatistic(
  val totalVisitors: Int,
  val averageAge: Int,
  val knowsKotlin: Int,
  val knowsJava: Int,
  val knowsHtmx: Int,
  val dislikesJavascript: Int,
  val likesJavascript: Int = totalVisitors - dislikesJavascript,
) {
  companion object {
    fun fromVisitors(visitors: List<Visitor>) =
      VisitorStatistic(
        totalVisitors = visitors.size,
        averageAge = visitors.map { it.age }.average().toInt(),
        knowsKotlin = visitors.count { it.knowsKotlin },
        knowsJava = visitors.count { it.knowsJava },
        knowsHtmx = visitors.count { it.knowsHtmx },
        dislikesJavascript = visitors.count { it.dislikesJavascript }
      )
  }
}
