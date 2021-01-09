package com.github.aleperaltabazas.dex.cache.pokedex

import arrow.core.orNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.cache.Cache
import com.github.aleperaltabazas.dex.cache.RefreshRate
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.RegionalPokedexDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.domain.Game
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit

class RegionalPokedexCache(
    private val game: Game,
    private val pokeapiConnector: RestConnector,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : Cache<RegionalPokedexDTO>(
    name = "${game.name}-cache",
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 30,
        unit = TimeUnit.DAYS,
    )
) {
    private fun dexId(): String = game.dexId

    private fun build(dex: PokedexDTO): RegionalPokedexDTO = RegionalPokedexDTO(
        pokemons = dex.pokemonEntries.map {
            DexEntryDTO(
                name = it.pokemonSpecies.name,
                number = it.entryNumber,
            )
        },
        region = game.region,
        type = game.type,
        game = GameDTO(
            title = game.title,
            fullTitle = game.fullTitle,
            spritePokemon = game.spritePokemon,
        )
    )

    override fun doRefresh(): RegionalPokedexDTO? = pokeapiConnector.get("/api/v2/pokedex/${this.dexId()}")
        .orNull()
        ?.deserializeAs(POKEAPI_DEX_REF)
        ?.let(this::build)

    companion object {
        private val POKEAPI_DEX_REF = object : TypeReference<PokedexDTO>() {}
    }
}