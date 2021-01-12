package com.github.aleperaltabazas.dex.cache.pokedex

import arrow.core.orNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.cache.RefreshRate
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit

class RegionalPokedexCache(
    private val game: Game,
    private val pokeapiConnector: RestConnector,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : Cache<GamePokedex>(
    name = "${game.title}-cache",
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 30,
        unit = TimeUnit.DAYS,
    )
) {
    private fun build(dex: PokedexDTO): GamePokedex = GamePokedex(
        pokemon = dex.pokemonEntries.map { it.pokemonSpecies.name },
        type = PokedexType.REGIONAL,
        game = game,
    )

    override fun doRefresh(): GamePokedex? = pokeapiConnector.get("/api/v2/pokedex/${this.game.pokeapiId}")
        .orNull()
        ?.deserializeAs(POKEAPI_DEX_REF)
        ?.let(this::build)

    companion object {
        private val POKEAPI_DEX_REF = object : TypeReference<PokedexDTO>() {}
    }
}