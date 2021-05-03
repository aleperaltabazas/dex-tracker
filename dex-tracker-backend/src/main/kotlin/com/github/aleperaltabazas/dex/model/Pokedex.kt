package com.github.aleperaltabazas.dex.model

enum class PokedexType {
    NATIONAL,
    REGIONAL,
}

data class Pokedex(
    val name: String,
    val displayName: String,
    val region: String,
    val gen: Int,
    val type: PokedexType,
    val entries: List<Entry>,
)

data class Entry(
    val name: String,
    val number: Int,
)

data class Game(
    val name: String,
    val displayName: String,
    val gen: Int,
) {
    constructor(pokedex: Pokedex) : this(
        name = pokedex.name,
        displayName = pokedex.displayName,
        gen = pokedex.gen,
    )
}
