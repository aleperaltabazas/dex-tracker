package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.UserDexPokemon

data class UserDTO(
    val username: String?,
    val pokedex: List<UserDexDTO>,
    val mail: String?,
    val picture: String?,
)

data class UserDexDTO(
    val userDexId: String,
    val game: GameDTO,
    val type: PokedexType,
    val region: String,
    val name: String? = null,
    val pokemon: List<UserDexPokemon>,
    val caught: Int,
)

data class CreateUserDexDTO(
    val game: String,
    val type: PokedexType,
    val name: String?,
)

data class CaughtStatusDTO(
    val pokedexId: String,
    val dexNumber: Int,
    val caught: Boolean,
)

data class UpdateUserDTO(
    val username: String?,
)
