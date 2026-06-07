package com.hophey.controller


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(){
    val authController: AuthController by inject()
    val flightsController: FlightsController by inject()
    val favouritesController: FavouritesController by inject()

    routing {
        get("/test"){
            call.respond(mapOf("hello" to "world"))
        }

        authController.configure(this)

        flightsController.configure(this)

        favouritesController.configure(this)
    }
}