package com.falcon.service.container

import com.falcon.config.ContainerDetails
import com.falcon.docker.DockerClientProvider
import com.falcon.utils.DockerUtils
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import com.github.dockerjava.api.exception.DockerException

class DockerContainerService : IDockerContainerService {
    private val dockerClient = DockerClientProvider().getDockerClient()
    private val logger = Logger.getLogger(DockerContainerService::class.java)

    override fun listContainers(): List<ContainerDetails> {
        return try {
            val allContainers = dockerClient.listContainersCmd().withShowAll(true).exec()
            DockerUtils.mapToContainerDetails(allContainers)
        } catch (e: DockerException) {
            logger.error(ResponseMessages.listContainersFailed(e.message ?: "Unknown error"), e)
            throw e
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
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerStartFailed(containerId), e)
            throw e
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
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerStopFailed(containerId), e)
            throw e
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
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerRemoveFailed(containerId), e)
            throw e
        }
    }

    // Test pending
    override fun renameContainer(
        containerId: String,
        newName: String,
    ): Boolean {
        if (containerId.isBlank() || newName.isBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            val container = dockerClient.inspectContainerCmd("container_name").exec()

            dockerClient.renameContainerCmd(container.id).withName("new_container_name").exec()

            logger.info(ResponseMessages.containerRenamed(containerId, newName))
            true
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerRenameFailed(containerId, newName), e)
            throw e
        }
    }

    override fun getContainerInfo(containerId: String): ContainerDetails? {
        if (containerId.isBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return null
        }
        return try {
            val allContainers = listContainers()
            val containerInfo = DockerUtils.findContainerById(allContainers, containerId)
            logger.info(containerInfo.toString())
            if (containerInfo != null) {
                logger.info(ResponseMessages.containerInfoRetrieved(containerId))
            } else {
                logger.warn(ResponseMessages.containerInfoNotRetrieved(containerId))
            }
            containerInfo
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerInfoFailed(containerId), e)
            throw e
        }
    }
}
