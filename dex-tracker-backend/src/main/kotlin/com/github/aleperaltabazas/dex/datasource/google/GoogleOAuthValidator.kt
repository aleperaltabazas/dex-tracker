package com.github.aleperaltabazas.dex.datasource.google

import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.slf4j.LoggerFactory

class GoogleOAuthValidator(
    private val clientId: String,
    private val clientSecret: String,
) {
    private val transport = GoogleNetHttpTransport.newTrustedTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()

    fun validate(authCode: String) {
        try {
            val tokenResponse = GoogleAuthorizationCodeTokenRequest(
                transport,
                jsonFactory,
                "https://oauth2.googleapis.com/token",
                clientId,
                clientSecret,
                authCode,
                "postmessage",
            ).execute()

            val idToken = GoogleIdToken.parse(jsonFactory, tokenResponse.idToken)
                ?: throw UnauthorizedException("Failed to parse ID token from auth code response")

            if (clientId !in idToken.payload.audienceAsList) {
                throw UnauthorizedException("Invalid audience in Google ID token")
            }

            LOGGER.info("Google auth code validated successfully for subject: ${idToken.payload.subject}")
        } catch (e: UnauthorizedException) {
            throw e
        } catch (e: Exception) {
            LOGGER.error("Google auth code validation failed", e)
            throw UnauthorizedException("Invalid Google OAuth code: ${e.message}")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GoogleOAuthValidator::class.java)
    }
}
