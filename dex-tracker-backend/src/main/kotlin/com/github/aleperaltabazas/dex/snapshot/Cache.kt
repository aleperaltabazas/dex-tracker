package com.github.aleperaltabazas.dex.snapshot

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.connector.RestConnector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

data class RefreshRate(
    val value: Int,
    val unit: TimeUnit
)

abstract class Cache<T>(
    private val connector: RestConnector,
    private val endpoint: String,
    private val name: String,
    private val refreshRate: RefreshRate,
) {
    private val ref = object : TypeReference<List<T>>() {}
    private var ts: List<T> = emptyList()

    open fun start() {
        GlobalScope.launch {
            refresh()
            delay(refreshRate.unit.toSeconds(refreshRate.value.toLong()))
        }
    }

    private fun refresh() {
        val response = connector.get(endpoint)
            .fold(
                ifLeft = { throw  it },
                ifRight = { it }
            )

        if (response.isError()) {
            LOGGER.warn("Error refreshing snapshot $name: status {}, {}", response.status, response.body)
        } else {
            val elements = response.deserializeAs(ref)

            this.ts = elements
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Cache::class.java)
    }
}