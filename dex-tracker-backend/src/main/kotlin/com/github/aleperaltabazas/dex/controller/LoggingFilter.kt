package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.extension.headersMap
import com.github.aleperaltabazas.dex.extension.prettyHeaders
import org.slf4j.LoggerFactory
import spark.Spark.after
import spark.Spark.before

object LoggingFilter {
    private val IGNORED_PATHS: List<String> = listOf()
    private val IGNORE_REQUEST_BODY_PATHS: List<String> = listOf()
    private val IGNORE_RESPONSE_BODY_PATHS: List<String> = listOf()
    private val IGNORED_HEADERS: List<String> = listOf("Cookie", "Set-Cookie")
    private val LOGGER = LoggerFactory.getLogger(LoggingFilter::class.java)

    fun register() {
        before("*") { req, _ ->
            val requestPath = req.contextPath()
            val headers = req
                .headersMap()
                .filterKeys { it in IGNORED_HEADERS }
                .prettyHeaders()
            if (!IGNORED_PATHS.contains(requestPath)) {
                LOGGER.info("[REQUEST] ${req.requestMethod()} $requestPath")
                LOGGER.info("[REQUEST] Headers: [$headers]")

                if (!IGNORE_REQUEST_BODY_PATHS.any { requestPath.matches(it.toRegex()) }) {
                    LOGGER.info("[REQUEST] Body: ${req.body()}")
                }
            }
        }

        after("*") { req, res ->
            val requestPath = req.contextPath()
            if (!IGNORED_PATHS.contains(requestPath)) {
                val headers = req
                    .headersMap()
                    .filterKeys { it in IGNORED_HEADERS }
                    .prettyHeaders()

                LOGGER.info("[RESPONSE] ${res.status()}")
                LOGGER.info("[RESPONSE] Headers: [$headers]")

                if (!IGNORE_RESPONSE_BODY_PATHS.any { requestPath.matches(it.toRegex()) }) {
                    LOGGER.info("[RESPONSE] Body: ${req.body()}")
                }
            }
        }
    }
}