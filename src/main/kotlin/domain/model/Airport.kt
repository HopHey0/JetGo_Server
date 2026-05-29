package com.hophey.domain.model

import com.hophey.repository.tables.flightTables.Countries

data class Airport (
    val id: Int,
    val city: String,
    val fullName: String,
    val airportCode: String,
    val countryCode: String,
    val countryName: String
)

