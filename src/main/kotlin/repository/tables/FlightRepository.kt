package com.hophey.repository.tables

import com.hophey.domain.model.Flight
import com.hophey.repository.tables.flightTables.*
import com.hophey.repository.tables.seatsTables.FlightsSeats
import com.hophey.utils.Formatters.formatter
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDate
import java.time.ZoneOffset

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
            .where { (Flights.departureDate greaterEq LocalDate.parse(departureDate, formatter).atStartOfDay().atOffset(ZoneOffset.UTC)) and (Flights.departureDate lessEq LocalDate.parse(departureDate, formatter).atStartOfDay().atOffset(ZoneOffset.UTC).plusDays(1L)) }
            .andWhere { departureAirport[Airports.city] eq departureCity }
            .andWhere { arrivalAirport[Airports.city] eq arrivalCity }
            .andWhere { Flights.id inSubQuery freeSeatsSubquery }
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
                arrivalCountry[Countries.countryCode].alias("arrival_country_code"),
                arrivalAirport[Airports.airportCode].alias("arrival_airport_code"),
                arrivalAirport[Airports.utcDiff].alias("arrival_utc_diff"),
                Airlines.airLineName.alias("airline_name"),
                Airlines.airlineCode,
                Airlines.logoUrl,
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