package com.falcon.plugins

import com.falcon.docker.DockerClientProvider
import com.falcon.service.AuthConfigService
import com.falcon.service.DockerContainerService
import com.falcon.service.IDockerContainerService
import com.github.dockerjava.api.DockerClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val falconModule =
    module {

        single<DockerClient> {
            DockerClientProvider().getDockerClient()
        }
        single<IDockerContainerService>(named("docker-container")) { (DockerContainerService()) }

        factory { AuthConfigService(get()) }
    }
