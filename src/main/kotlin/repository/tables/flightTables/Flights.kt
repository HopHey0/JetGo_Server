package com.hophey.repository.tables.flightTables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object Flights : Table("flights") {
    val id = long("flight_id").autoIncrement()
    val aircraftId = integer("aircraft_id").references(Airlines.id)
    val departureAirportId = integer("departure_airport_id").references(Airports.id)
    val arrivalAirportId = integer("arrival_airport_id").references(Airports.id)
    val flightNum = varchar("flight_num", 50)
    val departureDate = timestampWithTimeZone("departure_date")
    val arrivalDate = timestampWithTimeZone("arrival_date")
    val price = decimal("price", 10, 2)

    override val primaryKey = PrimaryKey(id)
}