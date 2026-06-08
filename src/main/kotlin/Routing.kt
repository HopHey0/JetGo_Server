package com.hophey

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        authenticate("ClientA") {
            get("/auth"){
                val client = call.principal<UserIdPrincipal>()
                call.respondText("Hello world!")
            }
        }

        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}