package com.falcon

import com.falcon.routes.docker.containerRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureDockerContainerRoutes() {
    routing {
        route("container") {
            containerRoutes()
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
