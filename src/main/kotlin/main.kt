package com.hophey

import com.hophey.controller.configureRouting
import com.hophey.di.appModule
import com.hophey.plugins.configureContentNegotiation
import com.hophey.plugins.configureSecurity
import com.hophey.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}


fun Application.module() {
    appModule()
    configureSecurity()
    configureContentNegotiation()
    configureStatusPages()
    configureRouting()

}

// lol