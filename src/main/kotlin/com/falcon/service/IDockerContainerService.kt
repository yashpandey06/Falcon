package com.falcon.service

import com.falcon.config.ContainerDetails

interface IDockerContainerService {
    fun listContainers(): List<ContainerDetails>

    fun startContainer(containerId: String): Boolean

    fun stopContainer(containerId: String): Boolean

    fun removeContainer(containerId: String): Boolean
}
