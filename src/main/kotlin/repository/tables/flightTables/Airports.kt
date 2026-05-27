package com.hophey.repository.tables.flightTables

import org.jetbrains.exposed.v1.core.Table

object Airports : Table("airports") {
    val id = integer("airport_id").autoIncrement()
    val countryId = integer("country_id").references(Countries.id)
    val city = varchar("city", 100)
    val fullName = varchar("full_name", 50)
    val airportCode = varchar("airport_code", 5)
    val utcDiff = integer("utc_diff")

    override val primaryKey = PrimaryKey(id)
}