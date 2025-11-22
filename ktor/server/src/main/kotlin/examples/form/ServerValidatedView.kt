package com.github.frederikpietzko.examples.form

import com.github.frederikpietzko.components.form.FieldsetProps
import com.github.frederikpietzko.components.form.checkbox
import com.github.frederikpietzko.components.form.fieldset
import com.github.frederikpietzko.conferenceName
import com.github.frederikpietzko.idRef
import com.github.frederikpietzko.insertBase
import com.github.frederikpietzko.layout.BaseTemplate
import com.github.frederikpietzko.layout.CommonPage
import io.konform.validation.ValidationError
import io.konform.validation.messagesAtPath
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import kotlinx.html.*


class ServerValidatedRegisterPage(
  private val action: String = "/form/server-validation",
  private val validationAction: String = "/form/server-validation/validate",
  private val model: RegisterFormSubmission = RegisterFormSubmission(),
  private val errors: List<ValidationError> = emptyList(),
) : BaseTemplate<HTML>() {
  context(application: Application)
  override fun HTML.render() {
    insertBase(CommonPage("Server Validated Register")) {
      children {
        insertBase(
          ServerValidatedForm(
            action = action,
            validationAction = validationAction,
            model = model,
            errors = errors,
          )
        )
      }
    }
  }
}

class ServerValidatedForm(
  private val action: String = "/form/server-validation",
  private val validationAction: String = "/form/server-validation/validate",
  private val model: RegisterFormSubmission = RegisterFormSubmission(),
  private val errors: List<ValidationError> = emptyList(),
) : BaseTemplate<FlowContent>() {
  companion object {
    const val FORM_ID = "registerForm"
  }

  context(application: Application)
  override fun FlowContent.render() {
    form(classes = "flex flex-col gap-4 w-sm lg:w-lg m-auto") {
      id = FORM_ID
      attributes.hx {
        trigger = "submit"
        post = this@ServerValidatedForm.action
        target = FORM_ID.idRef
      }

      h4("text-2xl text-center font-bold text-primary") {
        +"Register as a ${application.conferenceName} Visitor"
      }
      fieldset(
        FieldsetProps(
          "Name",
          id = "name",
          name = "name",
          placeholder = "Max Mustermann",
          helperText = "Please enter your full name",
          error = errors.messagesAtPath(RegisterFormSubmission::name).joinToString(", ")
        )
      ) {
        attributes.hx {
          trigger = "change"
          post = validationAction
          target = "#name"
          swap = "outerHTML"
          select = "#name"
        }
        value = model.name
      }
      fieldset(
        FieldsetProps(
          "Age",
          id = "age",
          name = "age",
          type = InputType.number,
          helperText = "Please enter your age",
          error = errors.messagesAtPath(RegisterFormSubmission::age).joinToString(", ")
        )
      ) {
        attributes.hx {
          trigger = "change"
          post = validationAction
          target = "#age"
          swap = "outerHTML"
          select = "#age"
        }
        value = model.age.toString()
      }
      div("grid grid-cols-2 gap-2") {
        checkbox("Knows Kotlin", "knowsKotlin") {
          checked = model.knowsKotlin
        }
        checkbox("Knows Java", "knowsJava") {
          checked = model.knowsJava
        }
        checkbox("Knows HTMX", "knowsHtmx") {
          checked = model.knowsHtmx
        }
        checkbox("Dislikes JavaScript", "dislikesJavascript") {
          checked = model.dislikesJavascript
        }
      }
      button(classes = "btn btn-primary mt-4", type = ButtonType.submit) {
        +"Submit"
      }
    }
  }
}
