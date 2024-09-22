package com.falcon.service.container

import com.falcon.config.ContainerDetails

interface IDockerContainerService {
    fun listContainers(): List<ContainerDetails>

    fun startContainer(containerId: String?): Boolean

    fun stopContainer(containerId: String?): Boolean

    fun removeContainer(containerId: String?): Boolean

    fun renameContainer(
        containerId: String?,
        newName: String?,
    ): Boolean

    fun getContainerInfo(containerId: String?): ContainerDetails?

//    suspend fun streamCPUInfo(containerId: String, response: Writer)
}
