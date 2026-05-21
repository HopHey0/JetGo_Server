package com.hophey.controller

import com.hophey.di.AppContainer
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(){
    routing {
        get("/test"){
            call.respond(mapOf("hello" to "world"))
        }

        AppContainer.authController.configure(this)
    }
}