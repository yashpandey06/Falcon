package com.falcon.utils

object ResponseMessages {
    fun containerStarted(containerId: String) = "Container $containerId started"

    fun containerStartFailed(containerId: String) = "Failed to start container $containerId"

    fun containerStopped(containerId: String) = "Container $containerId stopped"

    fun containerStopFailed(containerId: String) = "Failed to stop container $containerId"

    fun containerRemoved(containerId: String) = "Container $containerId removed"

    fun containerRemoveFailed(containerId: String) = "Failed to remove container $containerId"

    fun listContainersFailed(message: String) = "Failed to list containers: $message"

    fun containerRenamed(containerId: String, newName: String) = "Container $containerId renamed to $newName"

    fun containerRenameFailed(containerId: String, newName: String) = "Failed to rename container $containerId to $newName"

    fun containerInfoFailed(containerId: String) = "Failed to retrieve container info for $containerId"

    fun containerInfoRetrieved(containerId: String) = "Container info retrieved for ID: $containerId"

    fun containerInfoNotRetrieved(containerId: String)="No container found with ID: $containerId"

    fun cpuUsageNotRetrieved(containerId: String) = "Failed to retrieve CPU usage for container $containerId"
    fun cpuUsageRetrieved(containerId: String) = "CPU usage retrieved for container $containerId"
    const val CONTAINER_ID_REQUIRED = "Container ID is required"
    const val UNKNOWN_COMMAND = "Unknown Command"
    const val UNKNOWN_STATUS = "Unknown Status"
    const val UNKNOWN_STATE = "Unknown State"
    const val UNKNOWN_IMAGE = "Unknown Image"
    const val UNKNOWN_IMAGE_ID = "Unknown Image ID"
    const val UNKNOWN_CONTAINER_NAME = "Unknown Name"
    const val UNKNOWN_CONTAINER_ID = "Unknown ID"
}
