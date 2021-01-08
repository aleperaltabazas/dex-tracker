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
    val name: String,
) {
    private val snapshotFilePath = "${fileSystemHelper.getHomeDirectory()}/dex-cache/$name.json"
    private val ref = object : TypeReference<T>() {}
    private var t: T? = null
        @Synchronized get
        @Synchronized set

    fun get(): T? = t

    open fun start() {
        if (fileSystemHelper.doesFileExist(snapshotFilePath)) {
            val json = fileSystemHelper.readFile(snapshotFilePath)
            this.t = objectMapper.convertValue(json, ref)
        } else {
            fileSystemHelper.createDirectoryIfItDoesNotExist("${fileSystemHelper.getHomeDirectory()}/dex-cache")
            refresh()
        }

        GlobalScope.launch {
            refresh()
            delay(refreshRate.unit.toSeconds(refreshRate.value.toLong()))
        }
    }

    open fun stop() {
        if (saveToDisk) {
            save(body = objectMapper.writeValueAsString(t))
        }
    }

    protected abstract fun doRefresh(): T?

    private fun refresh() {
        val newT = doRefresh()

        if (newT == null) {
            LOGGER.warn("There was an error refreshing snapshot $name")
        } else {
            this.t = newT

            t.takeIf { saveToDisk }?.let { save(objectMapper.writeValueAsString(it)) }
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