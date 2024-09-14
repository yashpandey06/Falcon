package com.falcon.service

interface IDockerImageService {
    fun listImages(): List<String>

    fun pullImage(imageName: String): Boolean

    fun removeImage(imageId: String): Boolean
}
