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

                /*
                /airports/search/?q=pattern API
                Return response with list of found airports
                 */
                get("/airports/search"){
                    val query = call.request.queryParameters["q"]
                        ?.trim()
                        ?.takeIf { it.length > 2 }
                        ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Query parameter must be at least 2 chars", 400))

                    val pattern = "%${query.lowercase()}%"

                    val airportsResponse = flightService.getAirports(pattern)

                    when (airportsResponse.airports.isEmpty()) {
                        true -> call.respond(HttpStatusCode.NoContent, ErrorResponse("No results found", 204))
                        false -> call.respond(airportsResponse)
                    }
                }
            }
        }
    }
}