package com.hophey.repository

import com.hophey.domain.model.Airport
import com.hophey.domain.model.Flight
import com.hophey.repository.tables.flightTables.Aircraft
import com.hophey.repository.tables.flightTables.Airlines
import com.hophey.repository.tables.flightTables.Airports
import com.hophey.repository.tables.flightTables.Countries
import com.hophey.repository.tables.flightTables.Flights
import com.hophey.repository.tables.seatsTables.FlightsSeats
import com.hophey.utils.Formatters
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.Random
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.lowerCase
import org.jetbrains.exposed.v1.core.or
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
    private val countryName = Countries.fullName.alias("country_full_name")

    fun getAirports(query: String): List<Airport> = transaction {
        airportCountryJoin()
            .where {
                        (Airports.city.lowerCase() like query) or
                        (Airports.fullName.lowerCase() like query) or
                        (Airports.airportCode.lowerCase() like query)
            }
            .limit(20)
            .map { row ->
                rowToAirport(row)
            }
    }

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
            .where {
                (Flights.departureDate greaterEq LocalDate.parse(departureDate, Formatters.formatter).atStartOfDay()
                    .atOffset(ZoneOffset.UTC)) and (Flights.departureDate lessEq LocalDate.parse(
                    departureDate,
                    Formatters.formatter
                ).atStartOfDay().atOffset(ZoneOffset.UTC).plusDays(1L))
            }
            .andWhere { departureAirport[Airports.airportCode] eq departureCity }
            .andWhere { arrivalAirport[Airports.airportCode] eq arrivalCity }
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

    private fun airportCountryJoin(): Query {
        return Airports
            .join(
                otherTable = Countries,
                joinType = JoinType.INNER,
                onColumn = Airports.countryId,
                otherColumn = Countries.id,
            )
            .select(
                Airports.id,
                Airports.city,
                Airports.fullName,
                Airports.utcDiff,
                Airports.airportCode,
                Countries.countryCode,
                countryName
            )
    }

    private fun rowToHotFlight(row: ResultRow) = Flight(
        id = row[Flights.id],
        flightNumber = row[Flights.flightNum],
        price = row[Flights.price].toDouble(),
        departureTime = row[Flights.departureDate].toString(),
        arrivalTime = row[Flights.arrivalDate].toString(),
        arrivalUtcDiff = row[arrivalAirport[Airports.utcDiff]],
        departureAirport = row[departureAirport[Airports.airportCode]],
        arrivalAirport = row[arrivalAirport[Airports.airportCode]],
        departureCity = row[departureAirport[Airports.city]],
        departureUtcDiff = row[departureAirport[Airports.utcDiff]],
        arrivalCity = row[arrivalAirport[Airports.city]],
        airlineName = row[Airlines.airLineName],
        airlineCode = row[Airlines.airlineCode],
        arrivalCountry = row[arrivalCountry[Countries.fullName]],
        airlineLogo = row[Airlines.logoUrl],
        discountRate = kotlin.random.Random.nextDouble(0.1, 0.8),
    )

    private fun rowToFlight(row: ResultRow) = Flight(
        id = row[Flights.id],
        flightNumber = row[Flights.flightNum],
        price = row[Flights.price].toDouble(),
        departureTime = row[Flights.departureDate].toString(),
        arrivalTime = row[Flights.arrivalDate].toString(),
        arrivalUtcDiff = row[arrivalAirport[Airports.utcDiff]],
        departureAirport = row[departureAirport[Airports.airportCode]],
        arrivalAirport = row[arrivalAirport[Airports.airportCode]],
        departureCity = row[departureAirport[Airports.city]],
        departureUtcDiff = row[departureAirport[Airports.utcDiff]],
        arrivalCity = row[arrivalAirport[Airports.city]],
        airlineName = row[Airlines.airLineName],
        airlineCode = row[Airlines.airlineCode],
        arrivalCountry = row[arrivalCountry[Countries.fullName]],
        airlineLogo = row[Airlines.logoUrl],
    )

    private fun rowToAirport(row: ResultRow) = Airport(
        id = row[Airports.id],
        city = row[Airports.city],
        fullName = row[Airports.fullName],
        airportCode = row[Airports.airportCode],
        countryCode = row[Countries.countryCode],
        countryName = row[countryName]
    )
}