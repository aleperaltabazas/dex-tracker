package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.PokedexType

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
