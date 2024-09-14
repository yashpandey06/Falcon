package com.falcon.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DockerClientBuilder

class DockerClientProvider {
    private val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()

    fun getDockerClient(): DockerClient = dockerClient
}
