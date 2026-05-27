package com.hophey.di

import com.hophey.controller.AuthController
import com.hophey.controller.FlightsController
import com.hophey.repository.TokenRepository
import com.hophey.repository.UserRepository
import com.hophey.repository.database.DatabaseFactory
import com.hophey.repository.tables.FlightRepository
import com.hophey.service.AuthService
import com.hophey.service.FlightService
import com.hophey.service.JwtService
import io.ktor.server.application.*

object AppContainer {
    val tokenRepository: TokenRepository by lazy { TokenRepository() }

    val userRepository: UserRepository by lazy { UserRepository() }

    val flightRepository: FlightRepository by lazy { FlightRepository() }

    val jwtService: JwtService by lazy { JwtService() }

    val flightService: FlightService by lazy { FlightService(flightRepository) }

    val authService: AuthService by lazy { AuthService(tokenRepository = tokenRepository, userRepository = userRepository, jwtService = jwtService) }

    val authController: AuthController by lazy { AuthController(authService) }

    val flightsController: FlightsController by lazy { FlightsController(flightService) }
}

fun Application.appModule(){
    DatabaseFactory.init()
}