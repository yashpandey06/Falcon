package com.falcon.plugins

import com.falcon.config.AuthConfig
import com.falcon.docker.DockerClientProvider
import com.github.dockerjava.api.DockerClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val falconModule =
    module {
        single {
            HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
        }
        single { AuthConfig(get()) }
        single<DockerClient> {
            DockerClientProvider().getDockerClient()
        }
    }
