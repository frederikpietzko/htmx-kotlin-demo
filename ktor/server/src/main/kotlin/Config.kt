package com.github.frederikpietzko

import io.ktor.server.application.*

val Application.conferenceName get() = environment.config.property("conference.name").getString()
val Application.conferenceYear get() = environment.config.property("conference.year").getString().toInt()
val Application.randomVisitors get() = environment.config.property("conference.randomVisitors").getString().toBoolean()
