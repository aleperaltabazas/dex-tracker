package com.github.aleperaltabazas.dex.connector

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

open class HttpResponse(
    open val headers: Map<String, String>,
    open val body: String?,
    open val status: Int,
    private val objectMapper: ObjectMapper
) {
    open fun isError(): Boolean = status.isError()

    open fun <T> deserializeAs(typeReference: TypeReference<T>?): T = objectMapper.readValue(body, typeReference)

    private fun Int.isError() = this in 400..599
}