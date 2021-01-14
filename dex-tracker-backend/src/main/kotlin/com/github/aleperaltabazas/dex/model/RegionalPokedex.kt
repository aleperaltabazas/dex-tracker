package com.github.aleperaltabazas.dex.model

import com.typesafe.config.Config

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
    val gen: Int,
) {
    constructor(config: Config) : this(
        title = config.getString("title"),
        fullTitle = config.getString("full-title"),
        spritePokemon = config.getString("sprite-pokemon"),
        region = config.getString("region"),
        pokeapiId = config.getString("pokeapi-id"),
        gen = config.getInt("gen")
    )
}

data class GamePokedex(
    val game: Game,
    val type: PokedexType,
    val pokemon: List<String>,
)
