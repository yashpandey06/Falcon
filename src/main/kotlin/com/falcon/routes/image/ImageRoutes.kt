package com.falcon.routes.image

import com.falcon.service.image.DockerImageService
import com.falcon.service.image.IDockerImageService
import com.falcon.utils.Constants
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import kotlin.system.measureTimeMillis

fun Route.imageRoutes(service: IDockerImageService) {
    val logger = Logger.getLogger(DockerImageService::class.java)

    // POST Routes
//    get {
//        val latency = measureTimeMillis {
//            val dockerImage = call.parameters["image"] ?: throw IllegalArgumentException("Image cannot be null")
//            println(dockerImage)
//            val imagesPulledSuccessfully = service.pullImage(dockerImage)
//            if (imagesPulledSuccessfully) {
//                call.respond(HttpStatusCode.OK, mapOf("message" to "Image pull succeeded"))
//            } else {
//                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Image pull failed"))
//            }
//        }
//        Logger.logRequestDetails(
//            logger,
//            operationType = Constants.OPERATION_PULL_IMAGE,
//            requestType = Constants.REQUEST_TYPE_GET,
//            requestCode = HttpStatusCode.OK.value,
//            latency = latency
//        )
//    }

    // DELETE Routes
    delete {
        val latency =
            measureTimeMillis {
                val dockerImage =
                    call.parameters["image"] ?: throw IllegalArgumentException("Image name cannot be null")
                val imageRemoved = service.removeImage(dockerImage)
                if (imageRemoved) {
                    call.respond(HttpStatusCode.OK, ResponseMessages.imageRemoved(dockerImage))
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_REMOVE_IMAGE,
            requestType = Constants.REQUEST_TYPE_DELETE,
            requestCode = HttpStatusCode.OK.value,
            latency = latency,
        )
    }
}
