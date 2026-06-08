package com.hophey.repository

import com.hophey.domain.model.Flight
import com.hophey.repository.tables.flightTables.Aircraft
import com.hophey.repository.tables.flightTables.Airlines
import com.hophey.repository.tables.flightTables.Airports
import com.hophey.repository.tables.flightTables.Countries
import com.hophey.repository.tables.flightTables.Flights
import com.hophey.repository.tables.userFavourites.UserFavourites
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.OffsetDateTime
import java.time.ZoneOffset

class FavouriteRepository {

    private val departureAirport = Airports.alias("departure_airport")
    private val arrivalAirport   = Airports.alias("arrival_airport")
    private val airlineCountry   = Countries.alias("airline_country")
    private val departureCountry = Countries.alias("departure_country")
    private val arrivalCountry   = Countries.alias("arrival_country")

    fun getFavourites(userId: Int): List<Flight> = transaction {
        UserFavourites
            .join(Flights, JoinType.INNER, UserFavourites.flightId, Flights.id)
            .join(departureAirport, JoinType.INNER, Flights.departureAirportId, departureAirport[Airports.id])
            .join(arrivalAirport,   JoinType.INNER, Flights.arrivalAirportId,   arrivalAirport[Airports.id])
            .join(Aircraft,         JoinType.INNER, Flights.aircraftId,          Aircraft.id)
            .join(Airlines,         JoinType.INNER, Aircraft.airlineId,          Airlines.id)
            .join(departureCountry, JoinType.INNER, departureAirport[Airports.countryId], departureCountry[Countries.id])
            .join(arrivalCountry,   JoinType.INNER, arrivalAirport[Airports.countryId],   arrivalCountry[Countries.id])
            .join(airlineCountry,   JoinType.INNER, Airlines.countryId,          airlineCountry[Countries.id])
            .select(
                Flights.id, Flights.flightNum, Flights.price,
                Flights.departureDate, Flights.arrivalDate,
                departureAirport[Airports.city].alias("departure_city"),
                departureAirport[Airports.airportCode].alias("departure_airport_code"),
                departureAirport[Airports.utcDiff].alias("departure_utc_diff"),
                arrivalAirport[Airports.city].alias("arrival_city"),
                arrivalAirport[Airports.airportCode].alias("arrival_airport_code"),
                arrivalAirport[Airports.utcDiff].alias("arrival_utc_diff"),
                arrivalCountry[Countries.fullName].alias("arrival_country_name"),
                Airlines.airLineName.alias("airline_name"),
                Airlines.airlineCode,
                Airlines.logoUrl,
            )
            .where { UserFavourites.userId eq userId }
            .map { rowToFlight(it) }
    }

    fun addFavourite(userId: Int, flightId: Long): Boolean = transaction {
        val exists = UserFavourites.selectAll()
            .where { (UserFavourites.userId eq userId) and (UserFavourites.flightId eq flightId) }
            .count() > 0
        if (exists) return@transaction false
        UserFavourites.insert {
            it[this.userId]   = userId
            it[this.flightId] = flightId
            it[this.savedAt]  = OffsetDateTime.now(ZoneOffset.UTC)
        }
        true
    }

    fun removeFavourite(userId: Int, flightId: Long): Boolean = transaction {
        UserFavourites.deleteWhere {
            (UserFavourites.userId eq userId) and (UserFavourites.flightId eq flightId)
        } > 0
    }

    private fun rowToFlight(row: ResultRow) = Flight(
        id             = row[Flights.id],
        flightNumber   = row[Flights.flightNum],
        price          = row[Flights.price].toDouble(),
        departureTime  = row[Flights.departureDate].toString(),
        arrivalTime    = row[Flights.arrivalDate].toString(),
        departureAirport = row[departureAirport[Airports.airportCode]],
        departureCity  = row[departureAirport[Airports.city]],
        departureUtcDiff = row[departureAirport[Airports.utcDiff]],
        arrivalAirport = row[arrivalAirport[Airports.airportCode]],
        arrivalCity    = row[arrivalAirport[Airports.city]],
        arrivalUtcDiff = row[arrivalAirport[Airports.utcDiff]],
        arrivalCountry = row[arrivalCountry[Countries.fullName]],
        airlineName    = row[Airlines.airLineName],
        airlineCode    = row[Airlines.airlineCode],
        airlineLogo    = row[Airlines.logoUrl],
    )
}