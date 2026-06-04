package com.hophey.di

import com.hophey.controller.AuthController
import com.hophey.controller.FlightsController
import com.hophey.controller.configureRouting
import com.hophey.plugins.configureContentNegotiation
import com.hophey.plugins.configureKoin
import com.hophey.plugins.configureSecurity
import com.hophey.plugins.configureStatusPages
import com.hophey.repository.TokenRepository
import com.hophey.repository.UserRepository
import com.hophey.repository.database.DatabaseFactory
import com.hophey.repository.FlightRepository
import com.hophey.service.AuthService
import com.hophey.service.FlightService
import com.hophey.service.JwtService
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module
import org.koin.ktor.ext.inject

fun appModule(config: ApplicationConfig) = module {
    single { JwtService(jwtSecret = config.property("ktor.jwt.secret").getString()) }
    single { TokenRepository() }
    single { UserRepository() }
    single { FlightRepository() }
    single { FlightService(get()) }
    single { AuthService(get(), get(), get()) }
    single { AuthController(get()) }
    single { FlightsController(get()) }
}

fun Application.appModule(){
    configureKoin()
    DatabaseFactory.init(environment.config)
    val jwtService: JwtService by inject()
    configureSecurity(jwtService)
    configureContentNegotiation()
    configureStatusPages()
    configureRouting()
}