package com.github.frederikpietzko

import com.github.frederikpietzko.components.Fragment
import com.github.frederikpietzko.layout.BaseTemplate
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.utils.io.charsets.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.consumers.delayed
import kotlinx.html.stream.HTMLStreamBuilder

fun String.static() = "/static/$this"
val String.idRef get() = "#$this"

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
  build: TTemplate.() -> Unit
) {
  template.application = application
  template.build()
  with(template) {
    apply()
  }
}
