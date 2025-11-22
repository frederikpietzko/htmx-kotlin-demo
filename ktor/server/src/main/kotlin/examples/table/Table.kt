package com.github.frederikpietzko.examples.table

import com.github.frederikpietzko.components.datatable.SortDirection
import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.examples.form.RegisterForm
import com.github.frederikpietzko.examples.form.RegisterFormSubmission
import com.github.frederikpietzko.insertBase
import com.github.frederikpietzko.layout.BaseTemplate
import com.github.frederikpietzko.respondBaseTemplate
import com.github.frederikpietzko.respondSnippetTemplate
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.html.FlowContent

fun Application.table() {
  install(Sessions) {
    val secretSignKey = hex("f8f9faebf0f1f2f3f4f5f6f7f8f9faeb")
    cookie<OrderingState>("TABLE_SESSION", SessionStorageMemory()) {
      cookie.path = "/"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
  }
  routing {
    route("/table") {
      get {
        val orderingState = call.sessions.get<OrderingState>() ?: OrderingState.default()
        val model = tableModel(orderingState)
        call.respondBaseTemplate(VisitorTablePage(model))
      }
      get("/searchTable") {
        val search = call.queryParameters["search"] ?: ""
        val orderingState = call.sessions.get<OrderingState>() ?: OrderingState.default()
        val model = tableModel(orderingState, search)
        call.respondSnippetTemplate(VisitorTable(model))
      }
      get("/add-sort") {
        val columnName = call.parameters["columnName"] ?: ""
        val orderingState = call.sessions.get<OrderingState>() ?: OrderingState.default()
        if (columnName.isBlank()) {
          call.respondSnippetTemplate(VisitorTable(tableModel(orderingState)))
          return@get
        }
        val sort = when (call.parameters["sort"]) {
          "asc" -> SortDirection.ASC
          "desc" -> SortDirection.DESC
          else -> SortDirection.NONE
        }
        orderingState.addOrder(columnName, sort)
        call.sessions.set(orderingState)
        call.respondSnippetTemplate(VisitorTable(tableModel(orderingState)))
      }
      get("/drawer") {
        call.respondSnippetTemplate(VisitorDrawerPage())
      }
      post("/add") {
        val formData = call.receive<RegisterFormSubmission>()
        VisitorManagement.addVisitor(formData.toVisitor())
        val model = tableModel(call.sessions.get<OrderingState>() ?: OrderingState.default())
        call.respondSnippetTemplate(object : BaseTemplate<FlowContent>() {
          init {
            application = call.application
          }

          context(application: Application)
          override fun FlowContent.render() {
            insertBase(RegisterForm("/table/add"))
            insertBase(VisitorTable(model) {
              attributes.hx { swapOob = "true" }
            })
          }
        })
      }
    }
  }
}

private fun tableModel(
  orderingState: OrderingState,
  search: String = ""
) = VisitorManagement
  .allVisitors()
  .sortedWith(orderingState.toComparator())
  .filter { it.name.contains(search, ignoreCase = true) }
  .let { visitors ->
    TableModel(
      visitors = visitors,
      orderingState = orderingState,
    )
  }

