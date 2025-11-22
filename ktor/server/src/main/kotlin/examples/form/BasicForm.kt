package com.github.frederikpietzko.examples.form

import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.respondBaseTemplate
import com.github.frederikpietzko.respondSnippetTemplate
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.basicForm() {
  routing {
    route("/form") {
      get {
        call.respondBaseTemplate(RegisterPage())
      }
      post {
        val formData = call.receive<RegisterFormSubmission>()
        VisitorManagement.addVisitor(formData.toVisitor())
        call.respondSnippetTemplate(RegisterForm())
      }
    }
  }
}


