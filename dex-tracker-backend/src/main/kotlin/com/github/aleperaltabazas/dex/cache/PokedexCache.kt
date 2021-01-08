package com.github.aleperaltabazas.dex.cache

import arrow.core.orNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.PokedexDTO
import com.github.aleperaltabazas.dex.dto.dex.PokemonDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.PokemonSpeciesDTO
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.utils.FileSystemHelper
import java.util.concurrent.TimeUnit
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO as PokeapiDexDTO

class PokedexCache(
    private val pokeapiConnector: RestConnector,
    private val game: Game,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper
) : Cache<PokedexDTO>(
    name = game.name,
    saveToDisk = true,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
    refreshRate = RefreshRate(
        value = 30,
        unit = TimeUnit.DAYS,
    )
) {
    override fun doRefresh(): PokedexDTO? = pokeapiConnector.get("/api/v2/pokedex/${game.dexId}")
        .orNull()
        ?.deserializeAs(POKEAPI_DEX_REF)
        ?.let { dex ->
            val pokemons = dex.pokemonEntries.map {
                PokemonDTO(
                    name = it.pokemonSpecies.name,
                    number = it.entryNumber,
                )
            }

            PokedexDTO(
                pokemons = pokemons,
                region = game.region,
                type = game.type,
                game = GameDTO(
                    title = game.title,
                    fullTitle = game.fullTitle,
                    spritePokemon = game.spritePokemon,
                )
            )
        }

    companion object {
        private val POKEMON_SPECIES_REF = object : TypeReference<PokemonSpeciesDTO>() {}
        private val POKEAPI_DEX_REF = object : TypeReference<PokeapiDexDTO>() {}
    }
}