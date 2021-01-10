package com.github.aleperaltabazas.dex.db.model

data class User(
    val id: Long?,
    val username: String,
    val pokedexes: List<Pokedex>,
)

data class Pokedex(
    val id: Long?,
    val game: String,
    val type: String,
    val region: String,
    val pokemons: List<DexPokemon>,
)

data class DexPokemon(
    val id: Long?,
    val name: String,
    val dexNumber: Int,
    val caught: Boolean,
)
