package com.github.aleperaltabazas.dex.dto.pokeapi

data class PokedexDTO(
    val name: String,
    val region: RegionDTO?,
    val pokemonEntries: List<PokemonEntryDTO>
)

data class RegionDTO(
    val name: String,
)

data class PokemonEntryDTO(
    val pokemonSpecies: SpeciesRefDTO,
    val entryNumber: Int,
)

data class SpeciesRefDTO(
    val name: String,
)
