package com.falcon.utils

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object AuthUtils {
    fun hashPassword(password: String): String {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        return hashedPassword
    }

    fun generateToken(email: String): String {
        val algorithm = Algorithm.HMAC256("your-secret-key")
        return JWT.create()
            .withIssuer("your-issuer")
            .withAudience("your-audience")
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(algorithm)
    }

    fun verifyPassword(
        inputPassword: String,
        storedPasswordHash: String,
    ): Boolean {
        val result = BCrypt.verifyer().verify(inputPassword.toCharArray(), storedPasswordHash.toCharArray())
        return result.verified
    }
}
