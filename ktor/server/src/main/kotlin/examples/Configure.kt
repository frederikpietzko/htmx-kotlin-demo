package com.github.frederikpietzko.examples

import com.github.frederikpietzko.examples.form.basicForm
import io.ktor.server.application.*

fun Application.configure() {
  index()
  basicForm()
}
