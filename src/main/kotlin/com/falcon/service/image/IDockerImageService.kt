package com.falcon.service.image

interface IDockerImageService {
    fun removeImage(imageId: String?): Boolean
}
