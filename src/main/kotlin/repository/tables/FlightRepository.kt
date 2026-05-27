package com.hophey.repository.tables

import com.hophey.domain.model.Flight
import com.hophey.repository.tables.flightTables.*
import com.hophey.repository.tables.seatsTables.FlightsSeats
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.OffsetDateTime

class FlightRepository {

    private val departureAirport = Airports.alias("departure_airport")
    private val arrivalAirport = Airports.alias("arrival_airport")
    private val airlineCountry = Countries.alias("airline_country")
    private val departureCountry = Countries.alias("departure_country")
    private val arrivalCountry = Countries.alias("arrival_country")


    fun getHotOffers(): List<Flight> = transaction {
        flightJoin()
            .orderBy(Random())
            .limit(5)
            .map { row -> rowToHotFlight(row) }
    }

    fun getFlights(
        departureDate: String,
        arrivalDate: String,
        departureCity: String,
        arrivalCity: String,
        personAmount: Int
    ): List<Flight> = transaction {

        val freeSeatsSubquery = FlightsSeats
            .select(FlightsSeats.flightId)
            .where { FlightsSeats.isOccupied eq false }
            .groupBy(FlightsSeats.flightId)
            .having { FlightsSeats.flightId.count() greaterEq personAmount.toLong() }

        flightJoin()
            .where { (Flights.departureDate greaterEq OffsetDateTime.parse(departureDate)) and (Flights.departureDate lessEq OffsetDateTime.parse(departureDate).plusDays(1)) }
            .where { (Flights.arrivalDate greaterEq OffsetDateTime.parse(arrivalDate)) and (Flights.arrivalDate lessEq OffsetDateTime.parse(arrivalDate).plusDays(1)) }
            .where { departureAirport[Airports.city] eq departureCity }
            .where { arrivalAirport[Airports.city] eq arrivalCity }
            .where { Flights.id inSubQuery freeSeatsSubquery }
            .map { row -> rowToFlight(row) }
    }

    private fun flightJoin() : Query {
        return Flights
            .join(departureAirport, JoinType.INNER, Flights.departureAirportId, departureAirport[Airports.id])
            .join(arrivalAirport, JoinType.INNER, Flights.arrivalAirportId, arrivalAirport[Airports.id])
            .join(Aircraft, JoinType.INNER, Flights.aircraftId, Aircraft.id)
            .join(Airlines, JoinType.INNER, Aircraft.airlineId, Airlines.id)
            .join(departureCountry, JoinType.INNER, departureAirport[Airports.countryId], departureCountry[Countries.id])
            .join(arrivalCountry, JoinType.INNER, arrivalAirport[Airports.countryId], arrivalCountry[Countries.id])
            .join(airlineCountry, JoinType.INNER, Airlines.countryId, airlineCountry[Countries.id])
            .select(
                Flights.id,
                Aircraft.regNumber,
                Aircraft.model,
                airlineCountry[Countries.countryCode].alias("airline_country_code"),
                airlineCountry[Countries.fullName].alias("airline_country_full_name"),
                departureCountry[Countries.fullName].alias("departure_country_full_name"),
                departureAirport[Airports.city].alias("departure_city"),
                departureAirport[Airports.airportCode].alias("departure_airport_code"),
                departureAirport[Airports.utcDiff].alias("departure_utc_diff"),
                arrivalAirport[Airports.city].alias("arrival_city"),
                arrivalCountry[Countries.fullName].alias("arrival_country_name"),
                arrivalAirport[Airports.airportCode].alias("arrival_airport_code"),
                arrivalAirport[Airports.utcDiff].alias("arrival_utc_diff"),
                Flights.flightNum,
                Flights.departureDate,
                Flights.arrivalDate,
                Flights.price
            )
    }

    private fun rowToHotFlight(row: ResultRow) = Flight(
        id = row[Flights.id],
        flightNumber = row[Flights.flightNum],
        price = row[Flights.price].toDouble(),
        departureTime = row[Flights.departureDate].toString(),
        arrivalTime = row[Flights.arrivalDate].toString(),
        departureAirport = row[departureAirport[Airports.airportCode]],
        arrivalAirport = row[arrivalAirport[Airports.airportCode]],
        departureCity = row[departureAirport[Airports.city]],
        arrivalCity = row[arrivalAirport[Airports.city]],
        airlineName = row[Airlines.airLineName],
        airlineCode = row[Airlines.airlineCode],
        arrivalCountryCode = row[arrivalCountry[Countries.countryCode]],
        airlineLogo = row[Airlines.logoUrl],
        discountRate = kotlin.random.Random.nextDouble(0.1, 0.8),
    )

    private fun rowToFlight(row: ResultRow) = Flight(
        id = row[Flights.id],
        flightNumber = row[Flights.flightNum],
        price = row[Flights.price].toDouble(),
        departureTime = row[Flights.departureDate].toString(),
        arrivalTime = row[Flights.arrivalDate].toString(),
        departureAirport = row[departureAirport[Airports.airportCode]],
        arrivalAirport = row[arrivalAirport[Airports.airportCode]],
        departureCity = row[departureAirport[Airports.city]],
        arrivalCity = row[arrivalAirport[Airports.city]],
        airlineName = row[Airlines.airLineName],
        airlineCode = row[Airlines.airlineCode],
        arrivalCountryCode = row[arrivalCountry[Countries.countryCode]],
        airlineLogo = row[Airlines.logoUrl],
    )
}