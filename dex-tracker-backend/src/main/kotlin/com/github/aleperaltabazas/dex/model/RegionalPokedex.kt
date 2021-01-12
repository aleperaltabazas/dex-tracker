package com.github.aleperaltabazas.dex.model

enum class PokedexType {
    NATIONAL,
    REGIONAL,
}

data class Game(
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
    val region: String,
    val pokeapiId: String,
    val nationalCutoff: Int,
)

data class GamePokedex(
    val game: Game,
    val type: PokedexType,
    val pokemon: List<String>,
)
