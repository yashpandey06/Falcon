package com.falcon.service.auth

interface IAuthenticationService {
    fun authenticate(
        email: String,
        password: String,
    ): String

    fun generateToken(email: String): String
}
