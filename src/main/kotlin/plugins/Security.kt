package com.hophey.plugins

import com.hophey.config.JwtConfig
import com.hophey.service.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity(
    jwtService: JwtService
) {
    install (Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.jwtRealm

            verifier(JwtService.verifier)

            validate { credential ->
                jwtService.validator(credential)
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
}