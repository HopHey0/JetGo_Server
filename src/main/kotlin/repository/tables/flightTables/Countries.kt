package com.hophey.repository.tables.flightTables

import org.jetbrains.exposed.v1.core.Table

object Countries : Table("countries") {
    val id = integer("id").autoIncrement()
    val fullName = varchar("full_name", 50)
    val countryCode = varchar("county_code", 5)

    override val primaryKey = PrimaryKey(id)
}