package com.falcon.service.container

import com.falcon.config.ContainerDetails
import com.falcon.config.ContainerMetrics
import com.falcon.docker.DockerClientProvider
import com.falcon.utils.DockerUtils
import com.falcon.utils.Logger
import com.falcon.utils.ResponseMessages
import com.github.dockerjava.api.exception.DockerException
import com.github.dockerjava.api.model.Statistics
import com.github.dockerjava.core.InvocationBuilder

class DockerContainerService : IDockerContainerService {
    private val dockerClient = DockerClientProvider().getDockerClient()
    private val logger = Logger.getLogger(DockerContainerService::class.java)

    override suspend fun listContainers(): List<ContainerDetails> {
        return try {
            val allContainers = dockerClient.listContainersCmd().withShowAll(true).exec()
            DockerUtils.mapToContainerDetails(allContainers)
        } catch (e: DockerException) {
            logger.error(ResponseMessages.listContainersFailed(e.message ?: "Unknown error"), e)
            throw e
        }
    }

    override suspend fun startContainer(containerId: String?): Boolean {
        if (containerId.isNullOrBlank()) {
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

    override suspend fun stopContainer(containerId: String?): Boolean {
        if (containerId.isNullOrBlank()) {
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

    override suspend fun removeContainer(containerId: String?): Boolean {
        if (containerId.isNullOrBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            if (stopContainer(containerId)) {
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

    override suspend fun renameContainer(
        containerId: String?,
        newName: String?,
    ): Boolean {
        if (containerId.isNullOrBlank() || newName.isNullOrBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return false
        }
        return try {
            val container = dockerClient.inspectContainerCmd(containerId).exec()

            dockerClient.renameContainerCmd(container.id).withName(newName).exec()

            logger.info(ResponseMessages.containerRenamed(containerId, newName))
            true
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerRenameFailed(containerId, newName), e)
            throw e
        }
    }

    override suspend fun getContainerInfo(containerId: String?): ContainerDetails? {
        if (containerId.isNullOrBlank()) {
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

    override suspend fun getContainerMetrics(containerId: String?): ContainerMetrics? {
        if (containerId.isNullOrBlank()) {
            logger.error(ResponseMessages.CONTAINER_ID_REQUIRED)
            return null
        }

        return try {
            val container = dockerClient.inspectContainerCmd(containerId).exec()

            val callback = InvocationBuilder.AsyncResultCallback<Statistics>()
            dockerClient.statsCmd(container.id).exec(callback)
            val stats = callback.awaitResult()

            val memoryStats = stats.memoryStats
            val cpuStats = stats.cpuStats
            val networks = stats.networks

            if (memoryStats == null || cpuStats == null || networks == null) {
                logger.error("Failed to retrieve complete stats for container: $containerId")
                return null
            }

            val mbUsage = (memoryStats.usage ?: 0) / 1024 / 1024
            val networkUsage =
                networks.map { (interfaceName, networkStats) ->
                    "$interfaceName: rxBytes=${networkStats.rxBytes}, txBytes=${networkStats.txBytes}"
                }.joinToString("; ")

            val cpuUsage = cpuStats.cpuUsage?.totalUsage ?: 0
            val systemCpuUsage = cpuStats.systemCpuUsage ?: 0
            val numberOfCores = Runtime.getRuntime().availableProcessors()
            val cpuUsagePercentage =
                if (systemCpuUsage > 0) {
                    (cpuUsage.toDouble() / systemCpuUsage.toDouble()) * numberOfCores * 100
                } else {
                    0.0
                }

            ContainerMetrics(
                containerId = containerId,
                memoryUsageMb = mbUsage,
                networkUsage = networkUsage,
                cpuUsagePercentage = cpuUsagePercentage,
                systemCpuUsage = systemCpuUsage,
            )
        } catch (e: DockerException) {
            logger.error(ResponseMessages.containerMetricsNotRetrieved(containerId), e)
            throw e
        }
    }
}
