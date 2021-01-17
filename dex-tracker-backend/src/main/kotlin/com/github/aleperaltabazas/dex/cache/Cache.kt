package com.github.aleperaltabazas.dex.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
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
    private val refreshRate: RefreshRate,
    private val saveToDisk: Boolean,
    private val fileSystemHelper: FileSystemHelper,
    private val objectMapper: ObjectMapper,
    private val ref: TypeReference<T>,
    val name: String,
) {
    private val snapshotFilePath = "${fileSystemHelper.getHomeDirectory()}/dex-cache/$name.json"
    private var t: T? = null
        @Synchronized get
        @Synchronized set

    open fun get(): T = t ?: throw IllegalStateException("$name has null value for its value")

    open fun start() {
        if (fileSystemHelper.doesFileExist(snapshotFilePath)) {
            val json = fileSystemHelper.readFile(snapshotFilePath)
            this.t = objectMapper.readValue(json, ref)
        } else {
            fileSystemHelper.createDirectoryIfItDoesNotExist("${fileSystemHelper.getHomeDirectory()}/dex-cache")
            refresh()
        }

        GlobalScope.launch {
            delay(refreshRate.unit.toSeconds(refreshRate.value.toLong()))
            refresh()
        }
    }

    open fun stop() {
        LOGGER.info("Stopping cache $name")
        if (saveToDisk) {
            save(body = objectMapper.writeValueAsString(t))
        }
    }

    protected abstract fun doRefresh(): T?

    private fun refresh() {
        LOGGER.debug("Refreshing $name")
        val newT = doRefresh()

        if (newT == null) {
            LOGGER.warn("There was an error refreshing snapshot $name")
        } else {
            this.t = newT

            t.takeIf { saveToDisk }?.let { save(objectMapper.writeValueAsString(it)) }
            LOGGER.debug("Finished refreshing $name")
        }
    }

    private fun save(body: String) = fileSystemHelper.createFile(
        content = body,
        path = snapshotFilePath
    )

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Cache::class.java)
    }
}