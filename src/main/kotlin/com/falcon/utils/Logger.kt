package com.falcon.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logger {
    fun <T> getLogger(forClass: Class<T>): Logger {
        return LoggerFactory.getLogger(forClass)
    }

    fun logRequestDetails(
        logger: Logger,
        operationType: String,
        requestType: String,
        requestCode: Int,
        latency: Long,
    ) {
        logger.info(
            "Operation: $operationType, Request Type: $requestType, Request Code: $requestCode, Latency: ${latency}ms",
        )
    }
}
