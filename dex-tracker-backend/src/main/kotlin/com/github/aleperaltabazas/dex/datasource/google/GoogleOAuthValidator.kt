package com.github.aleperaltabazas.dex.datasource.google

import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import org.slf4j.LoggerFactory

class GoogleOAuthValidator(
    private val verifier: GoogleIdTokenVerifier,
) {
    fun validate(token: String) {
        verifier.verify(token) ?: throw UnauthorizedException("Invalid google oauth token")
        LOGGER.info("Token is valid")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GoogleOAuthValidator::class.java)
    }
}