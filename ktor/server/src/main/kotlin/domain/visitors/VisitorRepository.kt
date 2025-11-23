package com.github.frederikpietzko.domain.visitors

import kotlinx.coroutines.flow.*
import java.util.*

object VisitorRepository {
  private val _visitors: MutableList<Visitor> =
    Collections.synchronizedList(
      mutableListOf(
        Visitor.iitsDefault("Victor", 36),
        Visitor.iitsDefault("Markus", 38),
        Visitor.iitsDefault("Frederik", 27),
      )
    )

  private val _visitorsFlow = MutableSharedFlow<List<Visitor>>()
  val visitors: SharedFlow<List<Visitor>> = _visitorsFlow.asSharedFlow()
  val lastVisitor: Flow<Visitor> = _visitorsFlow.transform {
    emit(_visitors.last())
  }

  suspend fun add(visitor: Visitor): Visitor {
    _visitors.add(visitor)
    _visitorsFlow.emit(_visitors)
    return visitor
  }

  fun allVisitors(): List<Visitor> {
    return _visitors.toList()
  }

  fun visitorCount(): Int {
    return _visitors.size
  }
}
