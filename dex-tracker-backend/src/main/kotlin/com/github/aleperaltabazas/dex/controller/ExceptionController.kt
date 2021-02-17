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
    private val corsOrigins: List<String>
) : Controller {
    override fun register() {
        exception(Exception::class.java) { e, req, res ->
            val status = when (e) {
                is ApiException -> e.status
                else -> 500
            }

            LOGGER.error("Error: {} - {}", status, e.message, e)

            if (req.servletPath().startsWith("/api/v1")) {

                val body = ErrorDTO(
                    status = status,
                    message = e.message,
                    stackTrace = e.stackTrace.toList().takeIf { env.isDev() }

                )
                res.body(objectMapper.writeValueAsString(body))
                res.status(status)
                res.header("content-type", "application/json")

                if (env == Env.DEV) {
                    if (req.headers("Origin") in corsOrigins) {
                        res.header("Access-Control-Allow-Origin", req.headers("Origin"))
                    }

                    res.header("Access-Control-Allow-Credentials", "true")
                }
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ExceptionController::class.java)
    }
}