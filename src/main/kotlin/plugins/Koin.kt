package com.hophey.plugins

import com.hophey.di.appModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            appModule(environment.config)
        )
    }
}