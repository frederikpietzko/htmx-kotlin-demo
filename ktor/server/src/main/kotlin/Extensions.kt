package com.github.frederikpietzko

import com.github.frederikpietzko.components.Fragment
import com.github.frederikpietzko.layout.BaseTemplate
import io.ktor.htmx.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.sse.*
import io.ktor.server.websocket.*
import io.ktor.sse.*
import io.ktor.utils.io.charsets.*
import io.ktor.websocket.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.TBODY
import kotlinx.html.consumers.delayed
import kotlinx.html.stream.HTMLStreamBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun String.static() = "/static/$this"
val String.idRef get() = "#$this"

@OptIn(ExperimentalContracts::class)
fun String?.hasError(): Boolean {
  contract {
    returns(true) implies (this@hasError != null)
  }
  return !this.isNullOrBlank()
}

suspend fun <TTemplate : BaseTemplate<FlowContent>> DefaultWebSocketServerSession.sendHtml(
  template: TTemplate,
  block: TTemplate.() -> Unit = {}
) {
  template.application = application
  template.block()
  with(template) {
    block()
  }
  val text = renderSnippet {
    with(template) {
      apply()
    }
  }
  send(Frame.Text(text))
}

suspend fun <TTemplate : BaseTemplate<FlowContent>> ServerSSESession.sendHtml(
  template: TTemplate,
  block: TTemplate.() -> Unit = {}
) {
  template.application = call.application
  template.block()
  with(template) {
    block()
  }
  val text = renderSnippet {
    with(template) {
      apply()
    }
  }
  send(ServerSentEvent(text))
}

suspend fun ServerSSESession.sendHtml(
  block: FlowContent.() -> Unit = {}
) {
  val text = renderSnippet {
    block()
  }
  send(ServerSentEvent(text))
}

var HxAttributes.wsConnect: String
  get() = this["ws-connect"] ?: ""
  set(value) {
    this["ws-connect"] = value
  }

var HxAttributes.sseConnect: String
  get() = this["sse-connect"] ?: ""
  set(value) {
    this["sse-connect"] = value
  }

var HxAttributes.sseSwap: String
  get() = this["sse-swap"] ?: ""
  set(value) {
    this["sse-swap"] = value
  }

suspend fun <TTemplate : BaseTemplate<HTML>> ApplicationCall.respondBaseTemplate(
  template: TTemplate,
  status: HttpStatusCode = HttpStatusCode.OK,
  body: TTemplate.() -> Unit = {}
) {
  template.application = application
  template.body()
  respondHtml(status) {
    with(template) {
      apply()
    }
  }
}

suspend fun <TTemplate : BaseTemplate<FlowContent>> ApplicationCall.respondSnippetTemplate(
  template: TTemplate,
  status: HttpStatusCode = HttpStatusCode.OK,
  body: TTemplate.() -> Unit = {}
) {
  template.application = application
  template.body()
  respondHtmlSnippet(status) {
    with(template) {
      apply()
    }
  }
}

fun renderSnippet(
  block: FlowContent.() -> Unit
) = buildString {
  HTMLStreamBuilder(this, false, false)
    .delayed()
    .let { Fragment(it).block() }
}

fun renderTableRow(block: TBODY.() -> Unit) = buildString {
  HTMLStreamBuilder(this, false, false)
    .delayed()
    .let { TBODY(emptyMap(), it).block() }
}

suspend fun ApplicationCall.respondHtmlSnippet(
  status: HttpStatusCode = HttpStatusCode.OK,
  block: FlowContent.() -> Unit
) {
  val text = buildString {
    HTMLStreamBuilder(this, false, false)
      .delayed()
      .let { Fragment(it).block() }
  }
  respond(TextContent(text, ContentType.Text.Html.withCharset(Charsets.UTF_8), status))
}

context(application: Application)
fun <TOuter, TTemplate : BaseTemplate<TOuter>> TOuter.insertBase(
  template: TTemplate,
  build: TTemplate.() -> Unit = {}
) {
  template.application = application
  template.build()
  with(template) {
    apply()
  }
}
