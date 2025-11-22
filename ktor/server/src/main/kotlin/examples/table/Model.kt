package com.github.frederikpietzko.examples.table

import com.github.frederikpietzko.components.datatable.SortDirection
import com.github.frederikpietzko.domain.visitors.Visitor
import kotlinx.serialization.Serializable


@Serializable
data class OrderedBy(val column: String, var sort: SortDirection = SortDirection.NONE)

@Serializable
data class OrderingState(val orderedBy: MutableList<OrderedBy>) {
  companion object {
    fun default() = OrderingState(mutableListOf(OrderedBy("name", SortDirection.ASC)))
  }

  fun addOrder(column: String, sort: SortDirection) {
    val existing = orderedBy.firstOrNull { it.column == column }
    if (existing != null) {
      existing.sort = sort
    } else {
      orderedBy.add(OrderedBy(column, sort))
    }
  }

  fun toComparator(): Comparator<Visitor> {
    return orderedBy.fold(Comparator { _, _ -> 0 }) { acc, orderedBy ->
      if (orderedBy.sort == SortDirection.NONE) acc
      else acc.then(compareBy<Visitor> {
        when (orderedBy.column) {
          "name" -> it.name
          "age" -> it.age
          else -> error("Unknown column: ${orderedBy.column}")
        }
      }.let { if (orderedBy.sort == SortDirection.ASC) it else it.reversed() })
    }
  }
}
