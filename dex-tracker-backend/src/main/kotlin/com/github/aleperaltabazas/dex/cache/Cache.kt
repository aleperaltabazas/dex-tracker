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
    private val ref = object : TypeReference<List<T>>() {}
    private var ts: List<T> = emptyList()
        @Synchronized get
        @Synchronized set

    fun find(f: (T) -> Boolean): T? = ts.find(f)

    open fun findAll(): List<T> = ts

    open fun findAll(f: (T) -> Boolean) = ts.filter(f)

    open fun count(): Int = findAll().count()

    open fun count(f: (T) -> Boolean) = findAll(f).count()

    open fun start() {
        if (fileSystemHelper.doesFileExist(snapshotFilePath)) {
            val json = fileSystemHelper.readFile(snapshotFilePath)
            this.ts = objectMapper.convertValue(json, ref)
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
            save(body = objectMapper.writeValueAsString(ts))
        }
    }

    protected abstract fun doRefresh(): List<T>?

    private fun refresh() {
        val newTs = doRefresh()

        if (newTs == null) {
            LOGGER.warn("There was an error refreshing snapshot $name")
        } else {

            this.ts = newTs

            ts.takeIf { saveToDisk }?.let { save(objectMapper.writeValueAsString(it)) }
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