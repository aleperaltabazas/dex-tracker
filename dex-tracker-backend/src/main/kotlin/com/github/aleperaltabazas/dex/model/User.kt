package com.github.aleperaltabazas.dex.model

data class User(
    val userId: Long,
    val username: String? = null,
    val pokedex: List<UserDex>
)

data class UserDex(
    val game: String,
    val type: PokedexType,
    val region: String,
    val pokemon: List<UserDexPokemon>
)

data class UserDexPokemon(
    val name: String,
    val dexNumber: Int,
    val caught: Boolean,
)
