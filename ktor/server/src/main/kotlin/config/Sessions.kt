package com.github.frederikpietzko.config

import com.github.frederikpietzko.examples.ssetable.SSETableSession
import com.github.frederikpietzko.examples.table.OrderingState
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSessions() {
  install(Sessions) {
    val secretSignKey = hex("f8f9faebf0f1f2f3f4f5f6f7f8f9faeb")
    cookie<OrderingState>("TABLE_SESSION", SessionStorageMemory()) {
      cookie.path = "/"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
    cookie<SSETableSession>("SSE_TABLE_SESSION", SessionStorageMemory()) {
      cookie.path = "/"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
  }
}
