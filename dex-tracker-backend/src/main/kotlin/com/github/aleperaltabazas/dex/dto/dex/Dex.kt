package com.github.aleperaltabazas.dex.dto.dex

data class PokemonDTO(
    val name: String,
    val number: Int,
)

data class GameDTO(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
)

data class RegionalPokedexDTO(
    val pokemons: List<PokemonDTO>,
    val region: String?,
    val type: String,
    val game: GameDTO
)

data class NationalPokedexDTO(
    val pokemons: List<PokemonDTO>,
    val type: String,
)
