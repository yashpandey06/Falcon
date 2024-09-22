package com.falcon.routes.container

import com.falcon.config.ContainerDetails
import com.falcon.service.container.DockerContainerService
import com.falcon.service.container.IDockerContainerService
import com.falcon.utils.Constants
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.system.measureTimeMillis

fun Route.containerRoutes(service: IDockerContainerService) {
    val logger = Logger.getLogger(DockerContainerService::class.java)
    // GET Routes
    get {
        val latency =
            measureTimeMillis {
                val containers = service.listContainers()
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

    get("/{id}") {
        val containerId =
            call.parameters["id"] ?: throw IllegalArgumentException(ResponseMessages.CONTAINER_ID_REQUIRED)

        var container: ContainerDetails? = null
        val latency =
            measureTimeMillis {
                container = service.getContainerInfo(containerId)
                if (container != null) {
                    call.respond(HttpStatusCode.OK, container!!)
                } else {
                    call.respond(HttpStatusCode.NotFound, ResponseMessages.containerInfoNotRetrieved(containerId))
                }
            }
        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_GET_CONTAINER,
            requestType = Constants.REQUEST_TYPE_GET,
            requestCode = if (container != null) HttpStatusCode.OK.value else HttpStatusCode.NotFound.value,
            latency = latency,
        )
    }

//    get("cpu/{id}") {
//        val containerId = call.parameters["id"]
//
//        if (containerId != null) {
//            try {
//                // Stream CPU info using respondTextWriter
//                call.respondTextWriter(contentType = ContentType.Text.Plain) {
//                    service.streamCPUInfo(containerId, this)
//                }
//            } catch (e: Exception) {
//                // Log error and respond with an error message if streaming fails
//                logger.error("Error streaming CPU info for container $containerId", e)
//                call.respond(HttpStatusCode.InternalServerError, "Failed to stream CPU info")
//            }
//        } else {
//            // Handle case where container ID is missing
//            call.respond(HttpStatusCode.BadRequest, ResponseMessages.CONTAINER_ID_REQUIRED)
//        }
//    }

    // POST Routes
    post("/start/{id}") {
        val containerId =
            call.parameters["id"] ?: throw IllegalArgumentException(ResponseMessages.CONTAINER_ID_REQUIRED)
        val latency =
            measureTimeMillis {
                val success = service.startContainer(containerId)
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
    }

    post("/stop/{id}") {
        val containerId =
            call.parameters["id"] ?: throw IllegalArgumentException(ResponseMessages.CONTAINER_ID_REQUIRED)

        val latency =
            measureTimeMillis {
                val success = service.stopContainer(containerId)
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
    }

    post("rename/{id}") {
        val containerId =
            call.parameters["id"] ?: throw IllegalArgumentException(ResponseMessages.CONTAINER_ID_REQUIRED)
        val newName = call.receiveText()
        val latency =
            measureTimeMillis {
                val success = service.renameContainer(containerId, newName)
                if (success) {
                    call.respond(HttpStatusCode.OK, ResponseMessages.containerRenamed(containerId, newName))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ResponseMessages.containerRenameFailed(containerId, newName),
                    )
                }
            }

        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_RENAME_CONTAINER,
            requestType = Constants.REQUEST_TYPE_POST,
            requestCode = HttpStatusCode.OK.value,
            latency = latency,
        )
    }

    // DELETE Routes
    delete("/{id}") {
        val containerId =
            call.parameters["id"] ?: throw IllegalArgumentException(ResponseMessages.CONTAINER_ID_REQUIRED)

        val latency =
            measureTimeMillis {
                val success = service.removeContainer(containerId)
                if (success) {
                    call.respond(HttpStatusCode.OK, ResponseMessages.containerRemoved(containerId))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ResponseMessages.containerRemoveFailed(containerId),
                    )
                }
            }
        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_REMOVE_CONTAINER,
            requestType = Constants.REQUEST_TYPE_DELETE,
            requestCode = HttpStatusCode.OK.value,
            latency = latency,
        )
    }
}
