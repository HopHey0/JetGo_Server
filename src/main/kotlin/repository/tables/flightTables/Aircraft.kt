package com.hophey.repository.tables.flightTables

import org.jetbrains.exposed.v1.core.Table

object Aircraft : Table("aircraft") {
    val id = integer("id").autoIncrement()
    val airlineId = integer("airline_id").references(Airlines.id)
    val model = varchar("model", 50)
    val regNumber = varchar("reg_number", 50)

    override val primaryKey = PrimaryKey(id)
}