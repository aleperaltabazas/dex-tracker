package com.github.aleperaltabazas.dex.datasource.google

import com.github.aleperaltabazas.dex.exception.UnauthorizedException
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GoogleOAuthValidator {
    fun validate(token: String) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=$token"))
            .GET()
            .build()

        val response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw UnauthorizedException("Invalid google oauth token")
        }

        LOGGER.info("Token is valid")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GoogleOAuthValidator::class.java)
        private val HTTP_CLIENT = HttpClient.newHttpClient()
    }
}
