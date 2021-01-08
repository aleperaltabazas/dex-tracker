package com.github.aleperaltabazas.dex.cache.pokedex

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.PokemonDTO
import com.github.aleperaltabazas.dex.dto.dex.RegionalPokedexDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.utils.FileSystemHelper

class RegionalPokedexCache(
    private val game: Game,
    pokeapiConnector: RestConnector,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : PokedexCache<RegionalPokedexDTO>(
    name = "${game.name}-cache",
    pokeapiConnector = pokeapiConnector,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
) {
    override fun dexId(): String = game.dexId

    override fun build(dex: PokedexDTO): RegionalPokedexDTO = RegionalPokedexDTO(
        pokemons = dex.pokemonEntries.map {
            PokemonDTO(
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
}