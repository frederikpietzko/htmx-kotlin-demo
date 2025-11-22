package com.github.frederikpietzko.domain.visitors

object VisitorManagement {
  private val visitorRepository = VisitorRepository

  fun addVisitor(visitor: Visitor) =
    visitorRepository.add(visitor)


  fun allVisitors() = visitorRepository.allVisitors()
}
