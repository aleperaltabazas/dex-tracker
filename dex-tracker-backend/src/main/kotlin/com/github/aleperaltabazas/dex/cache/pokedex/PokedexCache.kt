package com.github.aleperaltabazas.dex.cache.pokedex

import arrow.core.orNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.cache.RefreshRate
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO as PokeapiDexDTO

abstract class PokedexCache<Dex>(
    private val pokeapiConnector: RestConnector,
    name: String,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : Cache<Dex>(
    name = name,
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 30,
        unit = TimeUnit.DAYS,
    )
) {
    protected abstract fun dexId(): String

    protected abstract fun build(dex: PokeapiDexDTO): Dex

    override fun doRefresh(): Dex? = pokeapiConnector.get("/api/v2/pokedex/${this.dexId()}")
        .orNull()
        ?.deserializeAs(POKEAPI_DEX_REF)
        ?.let(this::build)

    companion object {
        private val POKEAPI_DEX_REF = object : TypeReference<PokeapiDexDTO>() {}
    }
}