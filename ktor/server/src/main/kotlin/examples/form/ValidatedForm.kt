package com.github.frederikpietzko.examples.form

import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.respondBaseTemplate
import com.github.frederikpietzko.respondSnippetTemplate
import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation
import io.konform.validation.constraints.maximum
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.minimum
import io.konform.validation.constraints.notBlank
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.validatedForm() {
  routing {
    route("/form/server-validation") {
      get {
        call.respondBaseTemplate(ServerValidatedRegisterPage())
      }
      post {
        val model = call.receive<RegisterFormSubmission>()
        val result = validateFormSubmission.validate(model)
        when (result) {
          is Invalid -> call.respondSnippetTemplate(
            ServerValidatedForm(
              model = model,
              errors = result.errors
            )
          )

          is Valid<*> -> {
            VisitorManagement.addVisitor(model.toVisitor())
            call.respondSnippetTemplate(ServerValidatedForm())
          }
        }
        post("/validate") {
          val model = call.receive<RegisterFormSubmission>()
          val result = validateFormSubmission.validate(model)
          call.respondSnippetTemplate(
            ServerValidatedForm(
              model = model,
              errors = result.errors
            )
          )
        }
      }
    }
  }
}

val validateFormSubmission = Validation<RegisterFormSubmission> {
  RegisterFormSubmission::name {
    notBlank()
    minLength(2)
  }
  RegisterFormSubmission::age {
    minimum(18)
    maximum(100)
  }
}
