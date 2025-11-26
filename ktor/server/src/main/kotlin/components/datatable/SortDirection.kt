package com.github.frederikpietzko.components.datatable

enum class SortDirection {
  ASC, DESC, NONE;

  fun next() = when (this) {
    ASC -> DESC
    DESC -> NONE
    NONE -> ASC
  }
}
