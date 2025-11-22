package com.github.frederikpietzko.config

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties

@OptIn(ExperimentalSerializationApi::class, InternalAPI::class)
fun Application.configureContentNegotiation() {
  install(ContentNegotiation) {
    register(ContentType.Application.FormUrlEncoded, object : ContentConverter {
      override suspend fun serialize(
        contentType: ContentType, charset: Charset, typeInfo: TypeInfo, value: Any?
      ): OutgoingContent? {
        error { "Serialization to x-www-form-urlencoded is not supported" }
      }

      override suspend fun deserialize(
        charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel
      ): Any? {
        val text = content.readRemaining().readText(charset)
        val parameters = parseQueryString(text)
        val serializer = typeInfo.serializer()
        val stringMap = parameters.entries().associate { (key, values) ->
          key to (values.firstOrNull() ?: "")
        }
        return Properties.decodeFromStringMap(serializer, stringMap)
      }
    })
  }
}
