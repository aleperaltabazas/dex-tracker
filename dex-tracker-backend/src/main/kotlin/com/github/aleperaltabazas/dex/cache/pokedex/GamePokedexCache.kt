package com.github.aleperaltabazas.dex.cache.pokedex

import arrow.core.orNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.cache.RefreshRate
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.extension.sequence
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit

open class GamePokedexCache(
    private val games: List<Game>,
    private val pokeapiConnector: RestConnector,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : Cache<Map<String, GamePokedex>>(
    name = "regional-dex-cache",
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 30,
        unit = TimeUnit.DAYS,
    ),
    ref = object : TypeReference<Map<String, GamePokedex>>() {}
) {
    fun gameFromKey(gameKey: String): GamePokedex = this.get()
        .toList()
        .find { (g, _) -> g == gameKey }
        ?.second
        ?: throw NotFoundException("No game found for key $gameKey")

    private fun build(dex: PokedexDTO, game: Game): GamePokedex = GamePokedex(
        pokemon = dex.pokemonEntries
            .sortedBy { it.entryNumber }
            .map { it.pokemonSpecies.name },
        type = PokedexType.REGIONAL,
        game = game,
    )

    override fun doRefresh(): Map<String, GamePokedex>? = games.map { g ->
        pokeapiConnector.get("/api/v2/pokedex/${g.pokeapiId}")
            .map { it.deserializeAs(POKEAPI_DEX_REF) }
            .map { this.build(dex = it, game = g) }
            .map { g.title to it }
    }
        .sequence()
        .map { it.toMap() }
        .orNull()

    companion object {
        private val POKEAPI_DEX_REF = object : TypeReference<PokedexDTO>() {}
    }
}