package com.falcon.service.image

interface IDockerImageService {
    fun removeImage(imageId: String?): Boolean
    suspend fun pullImage(imageId: String?): Boolean
}
