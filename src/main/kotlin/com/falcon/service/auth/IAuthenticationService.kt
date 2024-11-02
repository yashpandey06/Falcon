package com.falcon.service.auth

import com.falcon.config.UserInfo

interface IAuthenticationService {
    suspend fun authenticate(
        email: String,
        password: String,
    ): String

    suspend fun registerUser(newUser: UserInfo)
}
