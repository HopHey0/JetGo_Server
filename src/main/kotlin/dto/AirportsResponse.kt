package com.hophey.dto

import com.hophey.domain.model.Airport
import kotlinx.serialization.Serializable


@Serializable
data class AirportDto(
    val id: Int,
    val city: String,
    val fullName: String,
    val airportCode: String,
    val countryCode: String,
    val countryName: String
)

@Serializable
data class AirportsResponse(
    val airports: List<AirportDto>
)

fun Airport.toDto(): AirportDto = AirportDto(
    id = this.id,
    city = this.city,
    fullName = this.fullName,
    airportCode = this.airportCode,
    countryCode = this.countryCode,
    countryName = this.countryName
)