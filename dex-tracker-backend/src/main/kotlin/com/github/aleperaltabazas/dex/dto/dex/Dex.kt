package com.github.aleperaltabazas.dex.dto.dex

data class PokemonDTO(
    val name: String,
    val number: Int,
    val caught: Boolean,
)

data class PokedexDTO(
    val pokemons: List<PokemonDTO>,
    val region: String,
    val type: String,
    val game: GameDTO
)

data class GameDTO(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
)
