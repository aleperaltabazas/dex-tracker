package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.*

data class FormDTO(
    val name: String,
) {
    constructor(form: Form) : this(name = form.name)
}

data class DexEntryDTO(
    val name: String,
    val number: Int,
    val forms: List<FormDTO>
)

data class GameDTO(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
) {
    constructor(game: Game) : this(
        title = game.title,
        fullTitle = game.fullTitle,
        spritePokemon = game.spritePokemon,
    )
}

data class GamePokedexDTO(
    val pokemon: List<DexEntryDTO>,
    val region: String,
    val type: PokedexType,
    val game: GameDTO,
)

data class UserDTO(
    val username: String?,
    val pokedex: List<UserDexRefDTO>
)

data class CaughtStatusDTO(
    val pokedexId: String,
    val dexNumber: Int,
    val caught: Boolean
)

data class CreateUserDexDTO(
    val game: String,
    val type: PokedexType,
    val name: String?,
)

data class UserDexDTO(
    val userDexId: String,
    val game: GameDTO,
    val type: PokedexType,
    val region: String,
    val name: String? = null,
    val pokemon: List<UserDexPokemon>
)

data class UserDexRefDTO(
    val userDexId: String,
    val game: GameDTO,
    val name: String?,
    val caught: Int,
)
