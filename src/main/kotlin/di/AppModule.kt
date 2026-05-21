package com.hophey.di

import com.hophey.controller.AuthController
import com.hophey.controller.configureRouting
import com.hophey.domain.usecase.LoginUseCase
import io.ktor.server.application.Application

object AppContainer {
    val loginUseCase: LoginUseCase by lazy { LoginUseCase() }

    val authController: AuthController by lazy { AuthController(loginUseCase) }
}

fun Application.appModule(){

}