package com.falcon.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("user_mail")
    val email: String,
    @SerialName("user_password")
    val password: String,
)

@Serializable
data class AuthUserRequest(
    @SerialName("user_mail")
    val email: String,
    @SerialName("user_password")
    val password: String,
)
