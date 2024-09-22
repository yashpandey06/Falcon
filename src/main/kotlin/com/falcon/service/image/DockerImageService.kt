package com.falcon.service.image

import com.falcon.docker.DockerClientProvider
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import com.falcon.utils.ResponseMessages.UNKNOWN_IMAGE
import com.github.dockerjava.api.exception.DockerException

class DockerImageService : IDockerImageService {
    private val dockerClient = DockerClientProvider().getDockerClient()
    private val logger = Logger.getLogger(DockerImageService::class.java)

    override fun removeImage(imageId: String?): Boolean {
        if (imageId.isNullOrBlank()) {
            logger.error(UNKNOWN_IMAGE)
            return false
        }
        try {
            val image = dockerClient.inspectImageCmd("image_name:tag").exec()

            image.id?.let { dockerClient.removeImageCmd(it).exec() }

            logger.info(ResponseMessages.imageRemoved(imageId))
            return true
        } catch (e: DockerException) {
            logger.error(ResponseMessages.imageRemoveFailed(imageId), e)
            throw e
        }
    }
}
