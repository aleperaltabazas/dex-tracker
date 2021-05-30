package com.github.aleperaltabazas.dex.extension

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.exception.BadRequestException
import spark.Request
import spark.Response

fun Request.headersMap(): Map<String, String> = headers().map { it to this.headers(it) }.toMap()

fun Response.headersMap(): Map<String, String> = this.raw().let { res ->
    res.headerNames.map { it to res.getHeader(it) }.toMap()
}

fun Map<String, String>.prettyHeaders() = this.map { (k, v) -> "\"$k:${v}\"" }.joinToString(",")

fun Request.paramNotNull(param: String) = requireNotNull(this.params(param)) {
    throw BadRequestException("$param path param should not be null")
}

fun Request.dexToken(): String? = headers(DEX_TOKEN) ?: cookie(DEX_TOKEN)
