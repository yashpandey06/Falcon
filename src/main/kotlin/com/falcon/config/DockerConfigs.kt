package com.falcon.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContainerDetails(
    @SerialName("container_id") val id: String,
    @SerialName("container_name") val name: String,
    @SerialName("container_image") val image: String,
    @SerialName("container_image_id") val imageId: String,
    @SerialName("container_command") val command: String,
    @SerialName("container_created") val created: Long,
    @SerialName("container_status") val status: String,
    @SerialName("container_state") val state: String,
    @SerialName("container_ports") val ports: List<ContainerPort>,
    @SerialName("container_labels") val labels: Map<String, String>,
    @SerialName("container_network_settings") val networkSettings: Map<String, ContainerNetwork>,
    @SerialName("container_mounts") val mounts: List<ContainerMount>,
)

@Serializable
data class ContainerPort(
    @SerialName("ip_address") val ip: String,
    @SerialName("private_port") val privatePort: Int,
    @SerialName("public_port") val publicPort: Int,
    @SerialName("port_type") val type: String,
)

@Serializable
data class ContainerNetwork(
    @SerialName("network_id") val networkID: String,
    @SerialName("ip_address") val ipAddress: String,
    @SerialName("gateway") val gateway: String,
    @SerialName("mac_address") val macAddress: String,
)

@Serializable
data class ContainerMount(
    @SerialName("source_path") val source: String,
    @SerialName("destination_path") val destination: String,
    @SerialName("mount_mode") val mode: String,
    @SerialName("read_write") val rw: Boolean,
)

@Serializable
data class ContainerMetrics(
    @SerialName("container_id") val containerId: String,
    @SerialName("memory_usage_mb") val memoryUsageMb: Long,
    @SerialName("network_usage") val networkUsage: String,
    @SerialName("cpu_usage_percentage") val cpuUsagePercentage: Double,
    @SerialName("system_cpu_usage") val systemCpuUsage: Long,
)
