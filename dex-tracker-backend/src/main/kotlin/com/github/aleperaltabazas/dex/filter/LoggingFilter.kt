package com.github.aleperaltabazas.dex.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class LoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val path = request.requestURI
        val method = request.method

        if (method !in IGNORE_METHODS && IGNORED_PATHS.none { path.matches(it.toRegex()) }) {
            val headers = request.headerNames.toList()
                .filterNot { it in IGNORED_HEADERS }
                .joinToString(",") { "\"$it:${request.getHeader(it)}\"" }

            LOGGER.info("[REQUEST] $method $path")
            LOGGER.info("[REQUEST] Headers: [$headers]")
        }

        filterChain.doFilter(request, response)

        if (method !in IGNORE_METHODS && IGNORED_PATHS.none { path.matches(it.toRegex()) }) {
            val headers = response.headerNames
                .filterNot { it in IGNORED_HEADERS }
                .joinToString(",") { "\"$it:${response.getHeader(it)}\"" }
            LOGGER.info("[RESPONSE] ${response.status}")
            LOGGER.info("[RESPONSE] Headers: [$headers]")
        }
    }

    companion object {
        private val IGNORED_PATHS: List<String> = listOf()
        private val IGNORE_METHODS = listOf("OPTIONS")
        private val IGNORED_HEADERS: List<String> = listOf("Cookie", "Set-Cookie", "dex-token")
        private val LOGGER = LoggerFactory.getLogger(LoggingFilter::class.java)
    }
}
