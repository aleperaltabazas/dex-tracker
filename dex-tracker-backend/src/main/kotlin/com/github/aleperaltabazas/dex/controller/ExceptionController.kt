package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.dto.dex.ErrorDTO
import com.github.aleperaltabazas.dex.exception.ApiException
import com.github.aleperaltabazas.dex.utils.Environment
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController(
    private val env: Environment,
) {
    @ExceptionHandler(Exception::class)
    fun handle(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorDTO> {
        val status = when (e) {
            is ApiException -> e.status
            else -> 500
        }

        LOGGER.error("Error: {} - {}", status, e.message, e)

        val body = ErrorDTO(
            status = status,
            message = e.message,
            stackTrace = e.stackTrace.toList().takeIf { env.isDev() },
        )

        return ResponseEntity.status(status).body(body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ExceptionController::class.java)
    }
}
