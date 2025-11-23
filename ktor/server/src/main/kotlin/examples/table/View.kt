package com.github.frederikpietzko.examples.table

import com.github.frederikpietzko.components.datatable.Column
import com.github.frederikpietzko.components.datatable.SortDirection
import com.github.frederikpietzko.components.datatable.dataTable
import com.github.frederikpietzko.domain.visitors.Visitor
import com.github.frederikpietzko.examples.form.RegisterForm
import com.github.frederikpietzko.insertBase
import com.github.frederikpietzko.layout.BaseTemplate
import com.github.frederikpietzko.layout.CommonPage
import io.ktor.htmx.*
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import kotlinx.html.*

class VisitorDrawerPage : BaseTemplate<FlowContent>() {
  context(_: Application)
  override fun FlowContent.render() {
    div("drawer drawer-end") {
      input(classes = "drawer-toggle", type = InputType.checkBox) {
        id = "drawer-toggle-1"
      }
      div("drawer-side z-100") {
        label("drawer-overlay") {
          htmlFor = "drawer-toggle-1"
          attributes["aria-label"] = "close sidebar"
        }
        div("z-10 bg-base-100 p-4 min-h-full text-base-content w-[800px]") {
          insertBase(RegisterForm("/table/add"))
        }
      }
    }
  }
}

class VisitorTablePage(
  private val model: TableModel,
) : BaseTemplate<HTML>() {

  context(_: Application)
  override fun HTML.render() {
    insertBase(CommonPage("Visitors")) {
      children {
        div("flex flex-col p-4 gap-3") {
          visitorSearchAndAddBar()
          div {
            attributes.hx {
              trigger = "load"
              get = "/table/drawer"
              swap = HxSwap.outerHtml
            }
          }
          insertBase(VisitorTable(model))
        }
      }
    }
  }
}

fun FlowContent.visitorSearchAndAddBar() {
  div("flex gap-2") {
    label("input w-full flex items-center") {
      i("h-[1em] opacity-50 fa fa-solid fa-search")
      input(InputType.search, classes = "grow") {
        id = "search"
        placeholder = "Search"
        name = "search"
        attributes.hx {
          trigger = "keyup changed delay:500ms"
          get = "/table/searchTable"
          target = "#visitorTable"
          swap = HxSwap.outerHtml
        }
      }
    }

    label("drawer-button btn btn-primary") {
      htmlFor = "drawer-toggle-1"
      +"Add"
    }
  }
}


class VisitorTable(
  private val model: TableModel,
  private val block: DIV.() -> Unit = { }
) : BaseTemplate<FlowContent>() {

  context(_: Application)
  override fun FlowContent.render() {
    dataTable(model.visitors, columns(model.orderingState), id = "visitorTable", block)
  }
}

private fun columns(orderingState: OrderingState) = listOf(
  Column(
    "Name",
    Visitor::name,
    sortable = true,
    sortWith = "/table/add-sort",
    sortableName = "name",
    sortTarget = "#visitorTable",
    sortDirection = orderingState.orderedBy.toSortDirection("name")
  ),
  Column(
    "Age",
    Visitor::age,
    sortable = true,
    sortableName = "age",
    sortWith = "/table/add-sort",
    sortTarget = "#visitorTable",
    sortDirection = orderingState.orderedBy.toSortDirection("age")
  ),
  Column("Knows Kotlin", Visitor::knowsKotlin),
  Column("Knows Java", Visitor::knowsJava),
  Column("Knows htmx", Visitor::knowsHtmx),
  Column("Dislikes JavaScript", Visitor::dislikesJavascript),
)

private fun List<OrderedBy>.toSortDirection(column: String): SortDirection {
  return firstOrNull { it.column == column }?.sort ?: SortDirection.NONE
}

data class TableModel(
  val visitors: List<Visitor>,
  val orderingState: OrderingState,
)
