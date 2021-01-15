package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.Form
import com.github.aleperaltabazas.dex.model.Game

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
    val type: String,
    val game: GameDTO,
)
