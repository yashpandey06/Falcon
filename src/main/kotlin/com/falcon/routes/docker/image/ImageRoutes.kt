package com.falcon.routes.docker.image

import com.falcon.service.DockerImageService
import com.falcon.service.IDockerImageService
import com.falcon.utils.Constants
import com.falcon.utils.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.system.measureTimeMillis

fun Route.imageRoutes(service: IDockerImageService){
    val logger = Logger.getLogger(DockerImageService::class.java)

    get{
        val latency =
            measureTimeMillis {
                val images=service.listImages()
                call.respond(HttpStatusCode.OK, mapOf("images" to images))

            }
        Logger.logRequestDetails(
            logger,
            operationType = Constants.OPERATION_LIST_IMAGES,
            requestType = Constants.REQUEST_TYPE_GET,
            requestCode = HttpStatusCode.OK.value,
            latency = latency,
        )
    }
    post{
        val latency =
            measureTimeMillis {

            }
    }
    delete{
        val latency =
            measureTimeMillis {

            }
    }
}