package com.falcon

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.falcon.database.DatabaseClient
import com.falcon.plugins.falconModule
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin

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
                ignoreUnknownKeys = true
            },
        )
    }

    configureKoin()
    configureAuthentication()
    configureAuthRoutes()
    configureDockerContainerRoutes()
    configureDatabase()
}

fun Application.configureKoin() {
    startKoin {
        modules(falconModule)
    }
}

// Placed a dummy secret keys for the algorithm for the authentication with dummy mock DBService
fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "your-realm"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("your-secret-key"))
                    .withIssuer("your-issuer")
                    .withAudience("your-audience")
                    .build(),
            )
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.configureDatabase() {
    DatabaseClient.initDB()
}
