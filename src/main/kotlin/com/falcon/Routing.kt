package com.falcon

import com.falcon.routes.auth.authRoutes
import com.falcon.routes.container.containerRoutes
import com.falcon.routes.image.imageRoutes
import com.falcon.service.auth.IAuthenticationService
import com.falcon.service.container.IDockerContainerService
import com.falcon.service.image.IDockerImageService
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureDockerContainerRoutes() {
    val containerService: IDockerContainerService by inject(named("docker-container"))
    routing {
        route("container") {
            containerRoutes(containerService)
        }
    }
}

fun Application.configureDockerImageRoutes() {
    val imageService: IDockerImageService by inject(named("docker-image"))
    routing {
        route("image") {
            imageRoutes(imageService)
        }
    }
}

fun Application.configureAuthRoutes() {
    val authService: IAuthenticationService by inject()
    routing {
        route("auth") {
            authRoutes(authService)
        }
    }
}
