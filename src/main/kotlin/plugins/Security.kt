package com.hophey.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hophey.config.JwtConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond

fun Application.configureSecurity() {
    install (Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.jwtRealm

            verifier {
                JWT.require(Algorithm.HMAC256(JwtConfig.jwtSecret))
                    .withAudience(JwtConfig.jwtAudience)
                    .withIssuer(JwtConfig.jwtRealm)
                    .build()
            }

            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                val exp = credential.payload.expiresAt?.time ?: 0
                val now = System.currentTimeMillis()
                val isExpired = exp < now

                if (username != null && !isExpired){
                    JWTPrincipal(payload = credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
}