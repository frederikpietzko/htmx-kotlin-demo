package com.github.frederikpietzko.examples.form

import com.github.frederikpietzko.components.form.FieldsetProps
import com.github.frederikpietzko.components.form.checkbox
import com.github.frederikpietzko.components.form.fieldset
import com.github.frederikpietzko.conferenceName
import com.github.frederikpietzko.idRef
import com.github.frederikpietzko.insertBase
import com.github.frederikpietzko.layout.BaseTemplate
import com.github.frederikpietzko.layout.CommonPage
import io.ktor.htmx.*
import io.ktor.htmx.html.*
import io.ktor.server.application.*
import kotlinx.html.*


class RegisterForm(
  private val action: String = "/form",
) : BaseTemplate<FlowContent>() {
  companion object {
    const val FORM_ID = "registerForm"
  }

  context(application: Application)
  override fun FlowContent.render() {
    form(classes = "flex flex-col gap-4 w-sm lg:w-lg m-auto") {
      id = FORM_ID
      action = this@RegisterForm.action
      method = FormMethod.post
      attributes.hx {
        trigger = "submit"
        post = this@RegisterForm.action
        target = FORM_ID.idRef
        swap = HxSwap.outerHtml
      }

      h4("text-2xl text-center font-bold text-primary") {
        +"Register as a ${application.conferenceName} Visitor"
      }

      fieldset(
        FieldsetProps(
          "Name",
          name = "name",
          placeholder = "Max Mustermann",
          helperText = "Please enter your full name"
        )
      ) {
        required = true
      }
      fieldset(FieldsetProps("Age", name = "age", type = InputType.number, helperText = "Please enter your age")) {
        required = true
        min = "18"
        max = "100"
      }
      div("grid grid-cols-2 gap-2") {
        checkbox("Knows Kotlin", "knowsKotlin")
        checkbox("Knows Java", "knowsJava")
        checkbox("Knows HTMX", "knowsHtmx")
        checkbox("Dislikes JavaScript", "dislikesJavascript")
      }
      button(classes = "btn btn-primary mt-4", type = ButtonType.submit) {
        +"Submit"
      }
    }
  }

}

class RegisterPage(
  private val action: String = "/form"
) : BaseTemplate<HTML>() {
  context(application: Application)
  override fun HTML.render() {
    insertBase(CommonPage("Register")) {
      children {
        insertBase(RegisterForm(action))
      }
    }
  }
}
