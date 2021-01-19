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
    val pokedex: List<UserDex>
) {
    constructor(user: User) : this(username = user.username, pokedex = user.pokedex)
}

data class CaughtStatusDTO(
    val pokedexId: Long,
    val dexNumber: Int,
    val caught: Boolean
)
