package com.github.aleperaltabazas.dex.cache.pokedex

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.aleperaltabazas.dex.connector.RestConnector
import com.github.aleperaltabazas.dex.dto.dex.NationalPokedexDTO
import com.github.aleperaltabazas.dex.dto.dex.PokemonDTO
import com.github.aleperaltabazas.dex.dto.pokeapi.PokedexDTO
import com.github.aleperaltabazas.dex.utils.FileSystemHelper

class NationalPokedexCache(
    pokeapiConnector: RestConnector,
    fileSystemHelper: FileSystemHelper,
    objectMapper: ObjectMapper,
) : PokedexCache<NationalPokedexDTO>(
    name = "national-dex-cache",
    pokeapiConnector = pokeapiConnector,
    fileSystemHelper = fileSystemHelper,
    objectMapper = objectMapper,
) {
    override fun dexId(): String = "national"

    override fun build(dex: PokedexDTO): NationalPokedexDTO = NationalPokedexDTO(
        pokemons = dex.pokemonEntries.map {
            PokemonDTO(
                name = it.pokemonSpecies.name,
                number = it.entryNumber,
            )
        },
        type = "national",
    )
}