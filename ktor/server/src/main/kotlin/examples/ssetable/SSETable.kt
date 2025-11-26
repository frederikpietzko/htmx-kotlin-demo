package com.github.frederikpietzko.examples.ssetable

import com.github.frederikpietzko.components.datatable.Column
import com.github.frederikpietzko.components.ssedatatable.sseDataTable
import com.github.frederikpietzko.components.ssedatatable.sseTableRow
import com.github.frederikpietzko.domain.visitors.Visitor
import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.domain.visitors.VisitorRepository
import com.github.frederikpietzko.examples.form.RegisterForm
import com.github.frederikpietzko.examples.form.RegisterFormSubmission
import com.github.frederikpietzko.examples.table.VisitorDrawerPage
import com.github.frederikpietzko.layout.CommonPage
import com.github.frederikpietzko.renderTableRow
import com.github.frederikpietzko.respondBaseTemplate
import com.github.frederikpietzko.respondHtmlSnippet
import com.github.frederikpietzko.respondSnippetTemplate
import io.ktor.htmx.*
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

fun Application.sseTable() {
  routing {
    route("/sse-table") {
      get {
        val session = call.sessions.get<SSETableSession>() ?: SSETableSession()
        call.respondBaseTemplate(CommonPage("SSE Table")) {
          children {
            div("flex flex-col p-4 gap-3") {
              div("flex gap-2") {
                label("input w-full flex items-center") {
                  i("h-[1em] opacity-50 fa fa-solid fa-search")
                  input(InputType.search, classes = "grow") {
                    id = "search"
                    placeholder = "Search"
                    name = "search"
                    value = session.search
                    attributes.hx {
                      trigger = "keyup changed delay:500ms"
                      get = "/sse-table/searchTable"
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
              div {
                attributes.hx {
                  trigger = "load"
                  get = "/sse-table/drawer"
                  swap = HxSwap.outerHtml
                }
              }
              sseDataTable(
                id = "visitorTable",
                data = VisitorManagement
                  .allVisitors()
                  .filter { it.name.contains(session.search, ignoreCase = true) },
                columns = columns,
                sseConnect = "/sse-table/stream",
              )
            }
          }
        }
      }
      sse("/stream") {
        heartbeat {
          period = 1.seconds
          event = ServerSentEvent("")
        }
        VisitorRepository.lastVisitor.collect { visitor ->
          send(ServerSentEvent(renderTableRow {
            val session = call.sessions.get<SSETableSession>() ?: SSETableSession()
            if (visitor.name.contains(session.search, ignoreCase = true)) {
              sseTableRow(visitor, columns)
            }
          }))
        }
      }
      get("/drawer") {
        call.respondSnippetTemplate(VisitorDrawerPage("/sse-table/add"))
      }
      post("/add") {
        val formData = call.receive<RegisterFormSubmission>()
        VisitorManagement.addVisitor(formData.toVisitor())
        call.respondSnippetTemplate(RegisterForm("/sse-table/add"))
      }
      get("/searchTable") {
        val search = call.queryParameters["search"] ?: ""
        val session = call.sessions.get<SSETableSession>() ?: SSETableSession()
        call.sessions.set(session.copy(search = search))
        call.respondHtmlSnippet {
          sseDataTable(
            id = "visitorTable",
            data = VisitorManagement.allVisitors().filter { it.name.contains(search, ignoreCase = true) },
            columns = columns,
            sseConnect = "/sse-table/stream",
          )
        }
      }
    }
  }
}

private val columns = listOf(
  Column(headerName = "Name", value = Visitor::name),
  Column(headerName = "Age", value = Visitor::age),
  Column(headerName = "Knows Kotlin", value = Visitor::knowsKotlin),
  Column(headerName = "Knows Java", value = Visitor::knowsJava),
  Column(headerName = "Knows htmx", value = Visitor::knowsHtmx),
  Column(headerName = "Dislikes JavaScript", value = Visitor::dislikesJavascript),
)

@Serializable
data class SSETableSession(val search: String = "")
