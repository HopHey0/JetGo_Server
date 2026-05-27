package com.hophey.repository.tables.seatsTables

import com.hophey.repository.tables.flightTables.Aircraft
import org.jetbrains.exposed.v1.core.Table

object Seats : Table("seats") {
    val id = integer("seat_id").autoIncrement()
    val aircraftId = integer("aircraft_id").references(Aircraft.id)
    val seatClassId = integer("seat_class_id").references(SeatClass.id)
    val row = varchar("row", 10)
    val position = varchar("position", 10)

    override val primaryKey = PrimaryKey(id)
}
