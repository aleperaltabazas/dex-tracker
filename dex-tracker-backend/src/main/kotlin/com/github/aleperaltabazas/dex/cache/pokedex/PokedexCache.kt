package com.github.aleperaltabazas.dex.cache.pokedex

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.cache.RefreshRate
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Pokedex
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit

open class PokedexCache(
    private val storage: Storage,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : Cache<List<Pokedex>>(
    name = "regional-dex-cache",
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 1,
        unit = TimeUnit.HOURS,
    ),
    ref = object : TypeReference<List<Pokedex>>() {}
) {
    open fun pokedexOf(key: String): Pokedex = this.get()
        .find { it.name == key }
        ?: throw NotFoundException("No regional pokedex found for game $key")

    override fun doRefresh() = storage.query(Collection.POKEDEX)
        .findAll(POKEDEX_REF)

    companion object {
        private val POKEDEX_REF = object : TypeReference<Pokedex>() {}
    }
}