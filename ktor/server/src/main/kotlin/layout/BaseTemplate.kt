package com.github.frederikpietzko.layout

import io.ktor.server.application.*
import io.ktor.server.html.*

abstract class BaseTemplate<in TOuter> : Template<TOuter> {
  lateinit var application: Application

  override fun TOuter.apply() {
    with(application) {
      render()
    }
  }

  context(application: Application)
  abstract fun TOuter.render()
}
