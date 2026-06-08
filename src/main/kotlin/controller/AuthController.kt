package com.hophey.controller

import com.hophey.dto.AuthRequest
import com.hophey.dto.LogoutRequest
import com.hophey.dto.RefreshTokenRequest
import com.hophey.service.AuthService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AuthController(
    private val authService: AuthService
) {
    fun configure(routing: Routing){
        routing.apply {
            route("auth") {
                post("/login") {
                    val request = call.receive<AuthRequest>()
                    val loginRespond = authService.login(request.username, request.password)

                    when (loginRespond) {
                        null -> call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Invalid username or password")
                        )

                        else -> call.respond(loginRespond)
                    }
                }
                post("/register") {
                    val request = call.receive<AuthRequest>()
                    val registerRespond = authService.register(request.username, request.password)

                    when (registerRespond) {
                        null -> call.respond(
                            HttpStatusCode.Conflict,
                            mapOf("error" to "User already exists")
                        )
                        else -> call.respond(registerRespond)
                    }
                }
                post("/logout") {
                    val request = call.receive<LogoutRequest>()
                    val isLoggedOut = authService.logout(request.refreshToken)

                    when (isLoggedOut) {
                        true -> call.respond(HttpStatusCode.OK)
                        else -> call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Something went wrong")
                        )
                    }
                }
                post("/refresh") {
                    val request = call.receive<RefreshTokenRequest>()
                    val authResponse = authService.refresh(request.refreshToken)

                    when (authResponse) {
                        null -> call.respond(HttpStatusCode.Unauthorized,
                            mapOf("error" to "Invalid refresh token"))
                        else -> call.respond(authResponse)
                    }
                }
            }
        }
    }
}