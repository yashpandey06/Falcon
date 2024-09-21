package com.falcon.utils

import com.falcon.config.ContainerDetails
import com.falcon.config.ContainerMount
import com.falcon.config.ContainerNetwork
import com.falcon.config.ContainerPort
import com.github.dockerjava.api.model.Container

object DockerUtils {

    /**
     * Maps a list of Docker API Container objects to a list of ContainerDetails.
     *
     * @param containers List of Docker API Container objects.
     * @return List of ContainerDetails with mapped data.
     */
    fun mapToContainerDetails(containers: List<Container>): List<ContainerDetails> {
        return containers.map { container ->
            ContainerDetails(
                id = container.id ?: ResponseMessages.UNKNOWN_CONTAINER_ID,
                name = container.names?.firstOrNull()?.removePrefix("/") ?: ResponseMessages.UNKNOWN_CONTAINER_NAME,
                image = container.image ?: ResponseMessages.UNKNOWN_IMAGE,
                imageId = container.imageId ?: ResponseMessages.UNKNOWN_IMAGE_ID,
                command = container.command ?: ResponseMessages.UNKNOWN_COMMAND,
                created = container.created ?: 0L,
                status = container.status ?: ResponseMessages.UNKNOWN_STATUS,
                state = container.state ?: ResponseMessages.UNKNOWN_STATE,
                ports = container.ports?.map { port ->
                    ContainerPort(
                        ip = port.ip ?: Constants.DEFAULT_IP,
                        privatePort = port.privatePort ?: Constants.DEFAULT_PORT,
                        publicPort = port.publicPort ?: Constants.DEFAULT_PORT,
                        type = port.type ?: Constants.DEFAULT_TYPE
                    )
                } ?: emptyList(),
                labels = container.labels ?: emptyMap(),
                networkSettings = container.networkSettings?.networks?.mapValues { entry ->
                    ContainerNetwork(
                        networkID = entry.value.networkID ?: Constants.DEFAULT_IP,
                        ipAddress = entry.value.ipAddress ?: Constants.DEFAULT_IP,
                        gateway = entry.value.gateway ?: Constants.DEFAULT_IP,
                        macAddress = entry.value.macAddress ?: Constants.DEFAULT_IP
                    )
                } ?: emptyMap(),
                mounts = container.mounts?.map { mount ->
                    ContainerMount(
                        source = mount.source ?: Constants.DEFAULT_IP,
                        destination = mount.destination ?: Constants.DEFAULT_IP,
                        mode = mount.mode ?: Constants.DEFAULT_MODE,
                        rw = mount.rw ?: Constants.DEFAULT_RW
                    )
                } ?: emptyList()
            )
        }
    }

    /**
     * Finds a container by ID from a list of ContainerDetails.
     *
     * @param containers List of ContainerDetails.
     * @param containerId The ID of the container to find.
     * @return The ContainerDetails if found, otherwise an empty ContainerDetails object.
     */
    fun findContainerById(containers: List<ContainerDetails>, containerId: String): ContainerDetails? {
        val containerInfo = containers.find { it.name == containerId }
        return containerInfo
    }
}