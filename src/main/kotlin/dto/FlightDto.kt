package com.hophey.dto

import com.hophey.domain.model.Flight
import kotlinx.serialization.Serializable

@Serializable
data class FlightDto(
    val id: Long,
    val flightNumber: String,
    val price: Double,
    val departureTime: String,
    val departureAirport: String,
    val departureCity: String,
    val departureUtcDiff: Int,
    val arrivalTime: String,
    val arrivalAirport: String,
    val arrivalCity: String,
    val arrivalCountry: String,
    val arrivalUtcDiff: Int,
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

fun Flight.toDto(): FlightDto {
    return FlightDto(
        id = this.id,
        flightNumber = this.flightNumber,
        price = this.price,
        departureTime = this.departureTime,
        departureAirport = this.departureAirport,
        departureCity = this.departureCity,
        departureUtcDiff = this.departureUtcDiff,
        arrivalTime = this.arrivalTime,
        arrivalAirport = this.arrivalAirport,
        arrivalCity = this.arrivalCity,
        arrivalCountry = this.arrivalCountry,
        arrivalUtcDiff = this.arrivalUtcDiff,
        airlineName = this.airlineName,
        airlineCode = this.airlineCode,
        airlineLogo = this.airlineLogo,
        discountRate = this.discountRate
    )
}

