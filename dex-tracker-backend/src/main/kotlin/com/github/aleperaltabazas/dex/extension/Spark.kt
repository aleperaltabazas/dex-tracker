package com.github.aleperaltabazas.dex.extension

import spark.Request
import spark.Response

fun Request.headersMap(): Map<String, String> = headers().map { it to this.headers(it) }.toMap()

fun Response.headersMap(): Map<String, String> = this.raw().let { res ->
    res.headerNames.map { it to res.getHeader(it) }.toMap()
}

fun Map<String, String>.prettyHeaders() = this.map { (k, v) -> "\"$k:${v}\"" }.joinToString(",")
