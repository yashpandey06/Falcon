package com.falcon.service

interface IDockerContainerService {
    fun listContainers(): List<String>

    fun startContainer(containerId: String): Boolean

    fun stopContainer(containerId: String): Boolean

    fun removeContainer(containerId: String): Boolean
}
