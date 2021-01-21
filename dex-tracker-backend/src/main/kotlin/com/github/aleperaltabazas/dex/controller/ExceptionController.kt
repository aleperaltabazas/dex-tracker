package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.dto.dex.ErrorDTO
import com.github.aleperaltabazas.dex.env.Env
import com.github.aleperaltabazas.dex.exception.ApiException
import org.slf4j.LoggerFactory
import spark.Spark.exception

class ExceptionController(
    private val objectMapper: ObjectMapper,
    private val env: Env,
) : Controller {
    override fun register() {
        exception(ApiException::class.java) { e, req, res ->
            if (req.servletPath().startsWith("/api/v1")) {
                LOGGER.error("error")
                val status = when (e) {
                    is ApiException -> e.status
                    else -> 500
                }
                val body = ErrorDTO(
                    status = status,
                    message = e.message,
                    stackTrace = e.stackTrace.toList().takeIf { env.isDev() }

                )
                res.body(objectMapper.writeValueAsString(body))
                res.status(status)
                res.header("content-type", "application/json")
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ExceptionController::class.java)
    }
}