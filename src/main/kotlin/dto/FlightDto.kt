package com.hophey.dto

import kotlinx.serialization.Serializable

@Serializable
data class FlightDto(
    val id: Long,
    val flightNumber: String,
    val price: Double,
    val departureTime: String,
    val departureAirport: String,
    val departureCity: String,
    val arrivalTime: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val arrivalCountryCode: String,
    val airlineName: String,
    val airlineCode: String,
    val airlineLogo: String,
    val discountRate: Double = 0.0
)

@Serializable
data class FlightsRequest(
    val departureCity: String,
    val arrivalCity: String,
    val departureDate: String,
    val personAmount: Int,
)

@Serializable
data class OffersResponse(
    val offers: List<FlightDto>
)


