package com.falcon.service.auth

import com.falcon.service.db.IDbService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class AuthenticationService:IAuthenticationService,KoinComponent {
    private val dbService : IDbService by inject()
    override fun authenticate(email:String,password:String): String {

        val user= dbService.findUserByEmail(email) ?: throw IllegalArgumentException("Invalid email or password")
        if(user.password==password){
            generateToken(email)
        }
        // TODO ("Proper handling of responses)
        return "Auth Failed"
    }

    override fun generateToken(email: String): String {
        val algorithm = Algorithm.HMAC256("your-secret-key")
        return JWT.create()
            .withIssuer("your-issuer")
            .withAudience("your-audience")
            .withClaim("username", email)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(algorithm)
    }
}