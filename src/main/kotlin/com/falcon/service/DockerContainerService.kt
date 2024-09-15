package com.falcon.service

import com.falcon.config.ContainerDetails
import com.falcon.docker.DockerClientProvider
import com.falcon.utils.DockerUtils
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages

class DockerContainerService : IDockerContainerService {
    private val dockerClient = DockerClientProvider().getDockerClient()
    private val logger = Logger.getLogger(DockerContainerService::class.java)

    override fun listContainers(): List<ContainerDetails> {
        return try {
            val allContainers = dockerClient.listContainersCmd().withShowAll(true).exec()
            DockerUtils.mapToContainerDetails(allContainers)
        } catch (e: Exception) {
            logger.error(ResponseMessages.listContainersFailed(e.message ?: "Unknown error"), e)
            emptyList()
        }
    }

    override fun startContainer(containerId: String): Boolean {
        if (containerId.isBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            dockerClient.startContainerCmd(containerId).exec()
            logger.info(ResponseMessages.containerStarted(containerId))
            true
        } catch (e: Exception) {
            logger.error(ResponseMessages.containerStartFailed(containerId), e)
            false
        }
    }

    override fun stopContainer(containerId: String): Boolean {
        if (containerId.isBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            dockerClient.stopContainerCmd(containerId).exec()
            logger.info(ResponseMessages.containerStopped(containerId))
            true
        } catch (e: Exception) {
            logger.error(ResponseMessages.containerStopFailed(containerId), e)
            false
        }
    }

    override fun removeContainer(containerId: String): Boolean {
        if (containerId.isBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            if (stopContainer(containerId)) {
                // Remove the container
                dockerClient.removeContainerCmd(containerId).exec()
                logger.info(ResponseMessages.containerRemoved(containerId))
                true
            } else {
                logger.error(ResponseMessages.containerRemoveFailed(containerId))
                false
            }
        } catch (e: Exception) {
            logger.error(ResponseMessages.containerRemoveFailed(containerId), e)
            false
        }
    }
}
