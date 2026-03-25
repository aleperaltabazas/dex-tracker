package com.github.aleperaltabazas.dex.filter

import com.github.aleperaltabazas.dex.extension.headersMap
import com.github.aleperaltabazas.dex.extension.prettyHeaders
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingFilter : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val path = request.requestURI
        val method = request.method

        if (method in IGNORE_METHODS || IGNORED_PATHS.any { path.matches(it.toRegex()) }) {
            return true
        }

        val headers = request.headersMap().filterKeys { it !in IGNORED_HEADERS }.prettyHeaders()
        LOGGER.info("[REQUEST] $method $path")
        LOGGER.info("[REQUEST] Headers: [$headers]")

        if (!IGNORE_REQUEST_BODY_PATHS.any { path.matches(it.toRegex()) }) {
            LOGGER.info("[REQUEST] Body: ${request.reader.readText()}")
        }

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val path = request.requestURI
        val method = request.method

        if (method in IGNORE_METHODS || IGNORED_PATHS.any { path.matches(it.toRegex()) }) return

        val headers = response.headersMap().filterKeys { it !in IGNORED_HEADERS }.prettyHeaders()
        LOGGER.info("[RESPONSE] ${response.status}")
        LOGGER.info("[RESPONSE] Headers: [$headers]")
    }

    companion object {
        private val IGNORED_PATHS: List<String> = listOf()
        private val IGNORE_REQUEST_BODY_PATHS: List<String> = listOf(
            "/api/v1/users", "/api/v1/games", "/api/v1/users/.*",
            "/api/v1/pokedex/.*", "/api/v1/login",
            ".*[.]html", ".*[.]css", ".*[.]js", ".*[.]png",
        )
        private val IGNORE_METHODS = listOf("OPTIONS")
        private val IGNORED_HEADERS: List<String> = listOf("Cookie", "Set-Cookie", "dex-token")
        private val LOGGER = LoggerFactory.getLogger(LoggingFilter::class.java)
    }
}
