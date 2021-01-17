package com.github.aleperaltabazas.dex.dto.dex

data class ErrorDTO(
    val status: Int,
    val message: String,
    val stackTrace: String?,
)
