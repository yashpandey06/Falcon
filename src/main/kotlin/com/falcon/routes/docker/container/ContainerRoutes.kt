package com.falcon.routes.docker.container

import com.falcon.service.DockerContainerService
import com.falcon.service.IDockerContainerService
import com.falcon.utils.Constants
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.system.measureTimeMillis

fun Route.containerRoutes(containerService: IDockerContainerService) {
    val logger = Logger.getLogger(DockerContainerService::class.java)
    // GET Routes
    get {
        val latency =
            measureTimeMillis {
                val containers = containerService.listContainers()
                call.respond(HttpStatusCode.OK, mapOf("containers" to containers))
            }
        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_LIST_CONTAINERS,
            requestType = Constants.REQUEST_TYPE_GET,
            requestCode = HttpStatusCode.OK.value,
            latency = latency,
        )
    }

    // POST Routes
    post("/start/{id}") {
        val containerId = call.parameters["id"]
        if (containerId != null) {
            val latency =
                measureTimeMillis {
                    val success = containerService.startContainer(containerId)
                    if (success) {
                        call.respond(HttpStatusCode.OK, ResponseMessages.containerStarted(containerId))
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ResponseMessages.containerStartFailed(containerId))
                    }
                }
            Logger.logRequestDetails(
                logger,
                operationType = Constants.OPERATION_START_CONTAINER,
                requestType = Constants.REQUEST_TYPE_POST,
                requestCode = HttpStatusCode.OK.value,
                latency = latency,
            )
        } else {
            call.respond(HttpStatusCode.BadRequest, ResponseMessages.CONTAINER_ID_REQUIRED)
        }
    }

    post("/stop/{id}") {
        val containerId = call.parameters["id"]
        if (containerId != null) {
            val latency =
                measureTimeMillis {
                    val success = containerService.stopContainer(containerId)
                    if (success) {
                        call.respond(HttpStatusCode.OK, ResponseMessages.containerStopped(containerId))
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ResponseMessages.containerStopFailed(containerId))
                    }
                }
            Logger.logRequestDetails(
                logger,
                operationType = Constants.OPERATION_STOP_CONTAINER,
                requestType = Constants.REQUEST_TYPE_POST,
                requestCode = HttpStatusCode.OK.value,
                latency = latency,
            )
        } else {
            call.respond(HttpStatusCode.BadRequest, ResponseMessages.CONTAINER_ID_REQUIRED)
        }
    }

    // DELETE Routes
    delete("/{id}") {
        val containerId = call.parameters["id"]
        if (containerId != null) {
            val latency =
                measureTimeMillis {
                    val success = containerService.removeContainer(containerId)
                    if (success) {
                        call.respond(HttpStatusCode.OK, ResponseMessages.containerRemoved(containerId))
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ResponseMessages.containerRemoveFailed(containerId))
                    }
                }
            Logger.logRequestDetails(
                logger,
                operationType = Constants.OPERATION_REMOVE_CONTAINER,
                requestType = Constants.REQUEST_TYPE_DELETE,
                requestCode = HttpStatusCode.OK.value,
                latency = latency,
            )
        } else {
            call.respond(HttpStatusCode.BadRequest, ResponseMessages.CONTAINER_ID_REQUIRED)
        }
    }
}
