package com.github.aleperaltabazas.dex.model

data class User(
    val id: Long? = null,
    val username: String? = null,
    val pokedex: List<UserDex>
)

data class UserDex(
    val id: Long? = null,
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
