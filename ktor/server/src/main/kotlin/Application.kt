package com.github.frederikpietzko

import com.github.frederikpietzko.config.configureDatabases
import com.github.frederikpietzko.config.configureHTTP
import com.github.frederikpietzko.config.configureMonitoring
import com.github.frederikpietzko.examples.configure
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureHTTP()
  configureDatabases()
  configureMonitoring()
  configure()
}
