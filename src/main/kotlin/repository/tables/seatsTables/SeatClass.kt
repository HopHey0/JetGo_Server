package com.hophey.repository.tables.seatsTables

import org.jetbrains.exposed.v1.core.Table

object SeatClass : Table("seat_class") {
    val id = integer("seat_class_id").autoIncrement()
    val name = varchar("name", 20)
    val seatClassMultiplier = decimal("seat_class_multiplier", 2, 1)
    override val primaryKey = PrimaryKey(id)
}