package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.PokedexType

data class GameDTO(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
    val region: String,
) {
    constructor(game: Game) : this(
        title = game.title,
        fullTitle = game.fullTitle,
        spritePokemon = game.spritePokemon,
        region = game.region,
    )
}

data class GamePokedexDTO(
    val pokemon: List<DexEntryDTO>,
    val type: PokedexType,
    val game: GameDTO,
)
