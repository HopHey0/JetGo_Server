package com.hophey.repository.tables.flightTables

import org.jetbrains.exposed.v1.core.Table

object Airlines : Table("airlines") {
    val id = integer("airline_id").autoIncrement()
    val countryId = integer("country_id").references(Countries.id)
    val airLineName = varchar("airline_name", 50)
    val logoUrl = text("logo_url")
    val airlineCode = varchar("airline_code", 10)

    override val primaryKey = PrimaryKey(id)
}