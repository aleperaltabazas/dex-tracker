package com.github.aleperaltabazas.dex.connector

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.exception.NetworkException
import com.typesafe.config.Config
import org.apache.http.Header
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.*
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import org.apache.http.HttpResponse as ApacheResponse

open class RestConnector(
    private val apacheClient: HttpClient,
    private val objectMapper: ObjectMapper,
    private val host: String
) {
    open fun get(path: String): Either<NetworkException, HttpResponse> = execute(HttpGet("$host/$path"))

    open fun post(path: String): Either<NetworkException, HttpResponse> = post(path, null)

    open fun post(path: String, body: Any?): Either<NetworkException, HttpResponse> = execute(HttpPost("$host/$path").also { it.setBody(body) })

    open fun put(path: String): Either<NetworkException, HttpResponse> = put(path, null)

    open fun put(path: String, body: Any?): Either<NetworkException, HttpResponse> = execute(HttpPut("$host/$path").also { it.setBody(body) })

    open fun patch(path: String): Either<NetworkException, HttpResponse> = patch(path, null)

    open fun patch(path: String, body: Any?): Either<NetworkException, HttpResponse> = execute(HttpPatch("$host/$path").also { it.setBody(body) })

    open fun delete(path: String, body: Any?): Either<NetworkException, HttpResponse> = execute(HttpDelete("$host/$path"))

    private fun execute(request: HttpRequestBase): Either<NetworkException, HttpResponse> = try {
        val response: ApacheResponse = apacheClient.execute(request)
        val body = response.readBody()
        val headers = response.allHeaders.map { Pair(it.name, it.value) }.toMap()
        val status = response.statusLine.statusCode
        request.releaseConnection()

        HttpResponse(
            headers = headers,
            body = body,
            status = status,
            objectMapper = objectMapper
        ).right()
    } catch (e: Throwable) {
        NetworkException(cause = e, message = "An error ocurred when executing the http request").left()
    }

    private fun HttpEntityEnclosingRequestBase.setBody(body: Any?): HttpEntityEnclosingRequestBase = body?.let { b ->
        val params = StringEntity(objectMapper.writeValueAsString(b), ContentType.APPLICATION_JSON)
        this.entity = params
        return this
    } ?: this

    private fun ApacheResponse.readBody(): String? = this.entity?.content
        ?.let { BufferedReader(InputStreamReader(it)) }
        ?.readLine()

    companion object {
        fun create(
            objectMapper: ObjectMapper,
            moduleConfig: Config,
            defaultHeaders: List<Header> = emptyList()
        ): RestConnector {
            return RestConnector(
                objectMapper = objectMapper,
                host = moduleConfig.getString("host"),
                apacheClient = HttpClientBuilder.create()
                    .setDefaultHeaders(defaultHeaders)
                    .build()
            )
        }
    }

}