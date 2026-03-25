package com.github.aleperaltabazas.dex.extension

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.exception.BadRequestException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

fun HttpServletRequest.headersMap(): Map<String, String> =
    headerNames.asSequence().map { it to getHeader(it) }.toMap()

fun HttpServletResponse.headersMap(): Map<String, String> =
    headerNames.map { it to getHeader(it) }.toMap()

fun Map<String, String>.prettyHeaders() = this.map { (k, v) -> "\"$k:${v}\"" }.joinToString(",")

fun HttpServletRequest.paramNotNull(param: String): String =
    getParameter(param) ?: throw BadRequestException("$param path param should not be null")

fun HttpServletRequest.pathVariable(name: String, uri: String): String {
    // Simple extraction helper — used via @PathVariable in controllers; kept for filter use
    return uri
}

fun HttpServletRequest.dexToken(): String? =
    getHeader(DEX_TOKEN) ?: cookies?.find { it.name == DEX_TOKEN }?.value
