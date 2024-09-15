package com.falcon

import com.falcon.routes.docker.container.containerRoutes
import com.falcon.service.IDockerContainerService
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
    routing {
        route("image") {
        }
    }
}

fun Application.configureDockerNetworkRoutes() {
    routing {
        route("network") {
        }
    }
}

fun Application.configureDockerVolumeRoutes() {
    routing {
        route("volume") {
        }
    }
}

fun Application.configureDockerRegistryRoutes() {
    routing {
        route("registry") {
        }
    }
}

fun Application.configureDockerStatsRoutes() {
    routing {
        route("stats") {
        }
    }
}
