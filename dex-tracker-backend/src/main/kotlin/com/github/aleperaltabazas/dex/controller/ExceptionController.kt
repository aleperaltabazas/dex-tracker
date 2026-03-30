package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.dto.dex.ErrorDTO
import com.github.aleperaltabazas.dex.exception.ApiException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController(
    @Value("\${env}") private val env: String,
) {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorDTO> {
        val status = when (e) {
            is ApiException -> e.status
            else -> 500
        }
        LOGGER.error("Error: {} - {}", status, e.message, e)

        val body = ErrorDTO(
            status = status,
            message = e.message,
            stackTrace = e.stackTrace.toList().takeIf { env == "dev" || env == "local" },
        )
        return ResponseEntity.status(status).body(body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ExceptionController::class.java)
    }
}
