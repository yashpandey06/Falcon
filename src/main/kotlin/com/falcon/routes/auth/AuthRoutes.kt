package com.falcon.routes.auth

import com.falcon.config.AuthUserRequest
import com.falcon.config.UserInfo
import com.falcon.service.auth.IAuthenticationService
import com.falcon.utils.AuthUtils
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.authRoutes(authService: IAuthenticationService) {
    post("/login") {
        val loginRequest = call.receive<AuthUserRequest>()
        val email = loginRequest.email
        val password = loginRequest.password
        if (email.isBlank()) {
            call.respondText("Email cannot be empty", status = HttpStatusCode.BadRequest)
            return@post
        }
        if (password.isBlank()) {
            call.respondText("Password cannot be empty", status = HttpStatusCode.BadRequest)
            return@post
        }

        try {
            val token = authService.authenticate(email, password)
            if (token != "Auth Failed") {
                call.respond(mapOf("token" to token, "success" to "Auth Done"))
            } else {
                call.respondText("Invalid email or password", status = HttpStatusCode.Unauthorized)
            }
        } catch (e: IllegalArgumentException) {
            call.respondText(e.message ?: "Authentication failed", status = HttpStatusCode.Unauthorized)
            throw e
        }
    }

    post("/register") {
        val registerRequest = call.receive<AuthUserRequest>()
        val email = registerRequest.email
        val password = registerRequest.password
        if (email.isBlank()) {
            call.respondText("Email cannot be empty", status = HttpStatusCode.BadRequest)
            return@post
        }
        if (password.isBlank()) {
            call.respondText("Password cannot be empty", status = HttpStatusCode.BadRequest)
            return@post
        }

        try {
            val hashedPassword = AuthUtils.hashPassword(password)
            val newUser =
                UserInfo(
                    email = email,
                    password = hashedPassword,
                )
            authService.registerUser(newUser)
            call.respond(mapOf("success" to "Registration Done"))
        } catch (e: Exception) {
            call.respondText(e.message ?: "Registration failed", status = HttpStatusCode.InternalServerError)
            throw e
        }
    }
}
