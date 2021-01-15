package com.github.aleperaltabazas.dex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.dto.dex.ErrorDTO
import com.github.aleperaltabazas.dex.exception.ApiException
import spark.Spark.exception

class ExceptionController(
    private val objectMapper: ObjectMapper
) : Controller {
    override fun register() {
        exception(ApiException::class.java) { e, req, res ->
            res.status(e.status)
            val body = ErrorDTO(
                status = e.status,
                message = e.message,
                stackTrace = e.stackTraceToString()
            )
            res.body(objectMapper.writeValueAsString(body))
        }
    }
}