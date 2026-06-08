package com.hophey.repository.tables.seatsTables

import com.hophey.repository.tables.flightTables.Flights
import org.jetbrains.exposed.v1.core.Table

object FlightsSeats : Table("flights_seats") {
    val id = long("flight_seat_id").autoIncrement()
    val flightId = long("flight_id").references(Flights.id)
    val seatId = integer("seat_id").references(Seats.id)
    val isOccupied = bool("is_occupied")

    override val primaryKey = PrimaryKey(id)
}