package com.falcon.service.auth

import com.falcon.config.UserInfo
import com.falcon.service.db.IDbService
import com.falcon.utils.AuthUtils.generateToken
import com.falcon.utils.AuthUtils.verifyPassword
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthenticationService : IAuthenticationService, KoinComponent {
    private val dbService: IDbService by inject()

    override suspend fun authenticate(
        email: String,
        password: String,
    ): String {
        try {
            val user = dbService.findUserByEmail(email) ?: throw IllegalArgumentException("Invalid email or password")
            val isValid = verifyPassword(password, user.password)
            return if (isValid) {
                generateToken(email)
            } else {
                "Auth Failed"
            }
        } catch (e: Exception) {
            throw Exception("Failed to authenticate user", e)
        }
    }

    override suspend fun registerUser(newUser: UserInfo) {
        val email = newUser.email
        if (dbService.findUserByEmail(email) != null) {
            throw IllegalArgumentException("User with this email already exists")
        }
        try {
            dbService.addNewUser(newUser)
        } catch (e: Exception) {
            throw Exception("Failed to register user", e)
        }
    }
}
