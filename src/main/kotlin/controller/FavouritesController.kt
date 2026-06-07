package com.hophey.controller

import com.hophey.service.FavouriteService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class FavouritesController(
    private val favouriteService: FavouriteService
) {
    fun configure(routing: Routing) {
        routing.apply {
            authenticate("auth-jwt") {
                route("favourites") {

                    get {
                        val email = call.principal<JWTPrincipal>()
                            ?.payload?.getClaim("username")?.asString()
                            ?: return@get call.respond(HttpStatusCode.Unauthorized)

                        val flights = favouriteService.getFavourites(email)
                            ?: return@get call.respond(HttpStatusCode.NotFound)

                        call.respond(mapOf("flights" to flights))
                    }

                    post("/{flightId}") {
                        val email = call.principal<JWTPrincipal>()
                            ?.payload?.getClaim("username")?.asString()
                            ?: return@post call.respond(HttpStatusCode.Unauthorized)

                        val flightId = call.parameters["flightId"]?.toLongOrNull()
                            ?: return@post call.respond(HttpStatusCode.BadRequest)

                        val added = favouriteService.addFavourite(email, flightId)
                            ?: return@post call.respond(HttpStatusCode.NotFound)

                        call.respond(if (added) HttpStatusCode.Created else HttpStatusCode.Conflict)
                    }

                    delete("/{flightId}") {
                        val email = call.principal<JWTPrincipal>()
                            ?.payload?.getClaim("username")?.asString()
                            ?: return@delete call.respond(HttpStatusCode.Unauthorized)

                        val flightId = call.parameters["flightId"]?.toLongOrNull()
                            ?: return@delete call.respond(HttpStatusCode.BadRequest)

                        val removed = favouriteService.removeFavourite(email, flightId)
                            ?: return@delete call.respond(HttpStatusCode.NotFound)

                        call.respond(if (removed) HttpStatusCode.OK else HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}