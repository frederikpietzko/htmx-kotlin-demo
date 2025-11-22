package com.github.frederikpietzko.examples

import com.github.frederikpietzko.examples.form.basicForm
import com.github.frederikpietzko.examples.form.validatedForm
import io.ktor.server.application.*

fun Application.configure() {
  index()
  basicForm()
  validatedForm()
}
