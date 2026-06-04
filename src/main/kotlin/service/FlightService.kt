package com.hophey.service

import com.hophey.dto.AirportsResponse
import com.hophey.dto.FlightsRequest
import com.hophey.dto.OffersResponse
import com.hophey.dto.toDto
import com.hophey.repository.FlightRepository

class FlightService(
    private val flightRepository: FlightRepository
) {
    fun getFlightOffers(
        flightsRequest: FlightsRequest
    ): OffersResponse {
        val listOfFlights = flightRepository.getFlights(
            departureDate = flightsRequest.departureDate,
            departureCity = flightsRequest.departureCity,
            arrivalCity = flightsRequest.arrivalCity,
            personAmount = flightsRequest.personAmount
        )

        return OffersResponse(
            listOfFlights.map { it.toDto() }
        )
    }

    fun getHotOffers(): OffersResponse {
        val listOfOffers = flightRepository.getHotOffers()
        return OffersResponse(
            listOfOffers.map { it.toDto() }
        )
    }

    fun getAirports(query: String): AirportsResponse {
        val listOfAirports = flightRepository.getAirports(query)
        return if (!listOfAirports.isEmpty()) {
            AirportsResponse(
                listOfAirports.map { it.toDto() }
            )
        } else {
            AirportsResponse(emptyList())
        }
    }
}