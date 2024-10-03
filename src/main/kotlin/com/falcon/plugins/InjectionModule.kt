package com.falcon.plugins

import com.falcon.docker.DockerClientProvider
import com.falcon.service.auth.AuthenticationService
import com.falcon.service.auth.IAuthenticationService
import com.falcon.service.container.DockerContainerService
import com.falcon.service.container.IDockerContainerService
import com.falcon.service.db.DbService
import com.falcon.service.db.IDbService
import com.falcon.service.image.DockerImageService
import com.falcon.service.image.IDockerImageService
import com.github.dockerjava.api.DockerClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val falconModule =
    module {

        single<DockerClient> {
            DockerClientProvider().getDockerClient()
        }
        single<IDockerContainerService>(named("docker-container")) { (DockerContainerService()) }
        single<IDockerImageService>(named("docker-image")) { DockerImageService() }
        factory<IDbService> { DbService() }
        factory<IAuthenticationService> { AuthenticationService() }
    }
