package com.falcon.service

import com.falcon.config.UserSession
import io.ktor.client.HttpClient
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.oauth
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

class AuthConfigService(private val httpClient: HttpClient) {
    fun configureSession(application: Application) {
        application.apply {
            install(Sessions) {
                cookie<UserSession>("user_session")
            }
            val redirects = mutableMapOf<String, String>()
            install(Authentication) {
                oauth("auth-oauth-google") {
                    urlProvider = { "http://localhost:8080/callback" }
                    providerLookup = {
                        io.ktor.server.auth.OAuthServerSettings.OAuth2ServerSettings(
                            name = "google",
                            authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                            accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                            requestMethod = io.ktor.http.HttpMethod.Post,
                            clientId = "",
                            clientSecret = "",
                            defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                            extraAuthParameters = listOf("access_type" to "offline"),
                            onStateCreated = { call, state ->
                                call.request.queryParameters["redirectUrl"]?.let {
                                    redirects[state] = it
                                }
                            },
                        )
                    }
                    client = httpClient
                }
            }
        }
    }
}
