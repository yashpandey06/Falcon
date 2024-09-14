package com.falcon

import com.falcon.config.AuthConfig
import com.falcon.plugins.falconModule
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureKoin()
        configureAuthentication()
    }.start(wait = true)
}

fun Application.configureKoin() {
    startKoin {
        modules(falconModule)
    }
}

fun Application.configureAuthentication() {
    val authConfig: AuthConfig by inject()
    authConfig.configureSession(this)
}
