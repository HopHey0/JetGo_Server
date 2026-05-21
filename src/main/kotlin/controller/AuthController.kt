package com.hophey.controller

import com.hophey.domain.usecase.LoginUseCase
import com.hophey.dto.LoginRequest
import com.hophey.dto.LoginResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AuthController(
    private val loginUseCase: LoginUseCase
) {
    fun configure(routing: Routing){
        routing.apply {
            post("/login") {
                val request = call.receive<LoginRequest>()
                val token = loginUseCase.login(request.username, request.password)

                when (token){
                    null -> call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid username or password"))
                    else -> call.respond(LoginResponse(token = token))
                }
            }
        }
    }
}