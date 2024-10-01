package com.falcon.utils

object Constants {
    // Container Operation Types
    const val OPERATION_LIST_CONTAINERS = "List Containers"
    const val OPERATION_START_CONTAINER = "Start Container"
    const val OPERATION_STOP_CONTAINER = "Stop Container"
    const val OPERATION_REMOVE_CONTAINER = "Remove Container"
    const val OPERATION_RENAME_CONTAINER = "Rename Container"
    const val OPERATION_GET_CONTAINER = "Get Specific Container"
    const val OPERATION_GET_CPU_USAGE = "Get CPU Usage For a Container"
    const val OPERATION_GET_CONTAINER_METRICS = "Container Metrics"

    // Images Operation Types
    const val OPERATION_REMOVE_IMAGE = "Remove Image"
    const val OPERATION_PULL_IMAGE = "Pull Image"

    // Request Types
    const val REQUEST_TYPE_GET = "GET"
    const val REQUEST_TYPE_POST = "POST"
    const val REQUEST_TYPE_DELETE = "DELETE"

    // Default Values
    const val DEFAULT_IP = "N/A"
    const val DEFAULT_PORT = 0
    const val DEFAULT_TYPE = "unknown"
    const val DEFAULT_MODE = "default"
    const val DEFAULT_RW = false
}
