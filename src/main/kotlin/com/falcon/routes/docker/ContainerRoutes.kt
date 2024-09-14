package com.falcon.routes.docker

import com.falcon.service.DockerContainerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.containerRoutes() {
    val containerService: DockerContainerService by inject()
    route("/container") {
        // GET Routes
        get {
            val containers = containerService.listContainers()
            call.respond(containers)
        }
        get("/{containerId}") {
            val containerId = call.parameters["containerId"]
            call.respond("$containerId")
        }

        // POST Routes
        post("/start/{id}") {
            val containerId = call.parameters["id"]
            if (containerId != null) {
                val success = containerService.startContainer(containerId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Container $containerId started")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to start container $containerId")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Container ID is required")
            }
        }

        post("/stop/{id}") {
            val containerId = call.parameters["id"]
            if (containerId != null) {
                val success = containerService.stopContainer(containerId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Container $containerId stopped")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to stop container $containerId")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Container ID is required")
            }
        }

        // DELETE Routes  (This route will also stop the container before removing it)
        delete("/{id}") {
            val containerId = call.parameters["id"]
            if (containerId != null) {
                val success = containerService.removeContainer(containerId)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Container $containerId removed")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to remove container $containerId")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Container ID is required")
            }
        }
    }
}
