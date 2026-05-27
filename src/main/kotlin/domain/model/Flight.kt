package com.hophey.domain.model

import com.hophey.dto.FlightResponse
import kotlinx.serialization.Serializable

data class Flight(
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


fun Flight.toSerializable(): FlightDto{
    return FlightDto(
        id = this.id,
        flightNumber = this.flightNumber,
        price = this.price,
        departureTime = this.departureTime,
        departureAirport = this.departureAirport,
        departureCity = this.departureCity,
        arrivalTime = this.arrivalTime,
        arrivalAirport = this.arrivalAirport,
        arrivalCity = this.arrivalCity,
        arrivalCountryCode = this.arrivalCountryCode,
        airlineName = this.airlineName,
        airlineCode = this.airlineCode,
        airlineLogo = this.airlineLogo,
        discountRate = this.discountRate
    )
}
