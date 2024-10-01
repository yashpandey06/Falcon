package com.falcon.service.container

import com.falcon.config.ContainerDetails
import com.falcon.config.ContainerMetrics

interface IDockerContainerService {
    suspend fun listContainers(): List<ContainerDetails>

    suspend fun startContainer(containerId: String?): Boolean

    suspend fun stopContainer(containerId: String?): Boolean

    suspend fun removeContainer(containerId: String?): Boolean

    suspend fun renameContainer(
        containerId: String?,
        newName: String?,
    ): Boolean

    suspend fun getContainerInfo(containerId: String?): ContainerDetails?

    suspend fun getContainerMetrics(containerId: String?): ContainerMetrics?
}
