package com.github.frederikpietzko.components.form

import kotlinx.html.*


fun FlowContent.checkbox(label: String, name: String, block: INPUT.() -> Unit = {}) =
  fieldSet("flex gap-1") {
    input(type = InputType.checkBox, name = name, classes = "checkbox") {
      block()
    }
    label("text-sm") {
      +label
    }
  }
