package com.github.frederikpietzko.components.ssedatatable

import com.github.frederikpietzko.components.datatable.Column
import com.github.frederikpietzko.sseConnect
import com.github.frederikpietzko.sseSwap
import io.ktor.htmx.html.*
import kotlinx.html.*

fun <T> FlowContent.sseDataTable(
  id: String? = null,
  data: List<T>,
  columns: List<Column<T>>,
  sseConnect: String,
) {
  div("overflow-x-auto rounded-box border border-base-content/5 bg-base-100") {
    id?.let { this.id = it }
    table("table") {
      thead {
        tr {
          for (column in columns) {
            th {
              +column.headerName
            }
          }
        }
      }
      tbody {
        attributes.hx {
          ext = "sse"
          this.sseConnect = sseConnect
          sseSwap = "message"
          swap = "beforeend"
        }
        for (row in data) {
          sseTableRow(row, columns)
        }
      }
    }
  }
}

fun <T> TBODY.sseTableRow(
  row: T,
  columns: List<Column<T>>,
) {
  tr {
    for (column in columns) {
      val value = column.value(row)
      td {
        when (value) {
          is String -> +value
          is Number -> +value.toString()
          is Boolean -> input(InputType.checkBox, classes = "checkbox checkbox-success") {
            disabled = true
            checked = value
          }
        }
      }
    }
  }
}
