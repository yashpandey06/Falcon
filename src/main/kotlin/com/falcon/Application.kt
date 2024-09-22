package com.falcon

import com.falcon.plugins.falconModule
import com.falcon.service.auth.AuthConfigService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            },
        )
    }

    configureKoin()
    configureDockerContainerRoutes()
}

fun Application.configureKoin() {
    startKoin {
        modules(falconModule)
    }
}

fun Application.configureAuthentication() {
    val authConfig: AuthConfigService by inject()
    authConfig.configureSession(this)
}
