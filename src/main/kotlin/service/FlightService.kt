package com.hophey.service

import com.hophey.domain.model.toSerializable
import com.hophey.dto.FlightsRequest
import com.hophey.dto.OffersResponse
import com.hophey.repository.tables.FlightRepository

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
            listOfFlights.map { it.toSerializable() }
        )
    }

    fun getHotOffers(): OffersResponse {
        val listOfOffers = flightRepository.getHotOffers()
        return OffersResponse(
            listOfOffers.map { it.toSerializable() }
        )
    }
}