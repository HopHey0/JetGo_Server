package com.hophey.controller

import com.hophey.dto.AuthRequest
import com.hophey.dto.ErrorResponse
import com.hophey.dto.FlightsRequest
import com.hophey.dto.LogoutRequest
import com.hophey.dto.RefreshTokenRequest
import com.hophey.service.AuthService
import com.hophey.service.FlightService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class FlightsController(
    private val flightService: FlightService
) {
    fun configure(routing: Routing) {
        routing.apply {
            route("flights") {
                get{
                    val flightsRequest = call.receive<FlightsRequest>()
                    val offersResponse = flightService.getFlightOffers(
                        flightsRequest = flightsRequest
                    )

                    when (offersResponse.offers.isEmpty()) {
                        true -> call.respond(HttpStatusCode.NoContent, ErrorResponse("No results found", 204))
                        false -> call.respond(offersResponse)
                    }

                }
                get("/hotOffers") {
                    val hotOffersResponse = flightService.getHotOffers()
                    when (hotOffersResponse.offers.isEmpty()) {
                        true -> call.respond(HttpStatusCode.NoContent, ErrorResponse("No results found", 204))
                        false -> call.respond(hotOffersResponse)
                    }
                }
            }
        }
    }
}