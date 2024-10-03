package com.falcon.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class UserSession(val state: String, val token: String)

@Serializable
data class UserInfo(
    @SerialName ("user_mail")
    val email:String,
    @SerialName("user_password")
    val password:String,
)
