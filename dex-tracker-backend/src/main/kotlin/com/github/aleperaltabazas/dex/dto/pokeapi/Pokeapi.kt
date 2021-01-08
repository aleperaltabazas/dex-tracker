package com.github.aleperaltabazas.dex.dto.pokeapi

data class PokedexDTO(
    val name: String,
    val region: RegionDTO?,
    val versionGroup: List<VersionGroupDTO>,
)

data class RegionDTO(
    val name: String,
)

data class VersionGroupDTO(
    val name: String,
)

data class PokemonEntryDTO(
    val entryNumber: Int,
    val pokemonSpecies: PokemonSpeciesDTO,
)

data class PokemonSpeciesDTO(
    val name: String,
    val url: String,
)