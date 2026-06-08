package com.hophey.repository.tables.userFavourites

import com.hophey.repository.tables.authTables.Users
import com.hophey.repository.tables.flightTables.Flights
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object UserFavourites : Table("user_favourites") {
    val id        = long("user_favourite_id").autoIncrement()
    val userId    = integer("user_id").references(Users.id)
    val flightId  = long("flight_id").references(Flights.id)
    val savedAt   = timestampWithTimeZone("saved_at")

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(userId, flightId)
    }
}