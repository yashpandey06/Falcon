package com.falcon.config

import kotlinx.serialization.Serializable

data class UserSession(val state: String, val token: String)

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    val picture: String,
)
