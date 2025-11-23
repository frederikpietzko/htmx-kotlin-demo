package com.github.frederikpietzko

import com.github.frederikpietzko.config.*
import com.github.frederikpietzko.examples.configure
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureContentNegotiation()
  configureSessions()
  configureDatabases()
  configureMonitoring()
  configureHTTP()
  configure()
}
