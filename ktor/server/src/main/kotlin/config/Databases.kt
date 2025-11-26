package com.github.frederikpietzko.config

import io.ktor.server.application.*
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

fun Application.configureDatabases() {
  val database = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "root",
    driver = "org.h2.Driver",
    password = "",
  )
  transaction {
    val statements = MigrationUtils.statementsRequiredForDatabaseMigration(*tables.toTypedArray())
    execInBatch(statements)
  }
}

val tables = listOf<Table>()
