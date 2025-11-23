package com.github.frederikpietzko

import com.github.frederikpietzko.config.*
import com.github.frederikpietzko.domain.visitors.Visitor
import com.github.frederikpietzko.domain.visitors.VisitorManagement
import com.github.frederikpietzko.examples.configure
import com.github.javafaker.Faker
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
  if (randomVisitors) {
    launch {
      val faker = Faker()
      while (true) {
        delay(5000)
        val visitor = Visitor(
          name = faker.name().fullName(),
          age = faker.number().numberBetween(18, 99),
          knowsKotlin = faker.bool().bool(),
          knowsHtmx = faker.bool().bool(),
          knowsJava = faker.bool().bool(),
          dislikesJavascript = faker.bool().bool(),
        )
        VisitorManagement.addVisitor(visitor)
      }
    }
  }
}
