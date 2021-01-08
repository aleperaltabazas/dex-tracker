package com.github.aleperaltabazas.dex.model

data class Game(
    val name: String,
    val dexId: String,
    val title: String,
    val fullTitle: String,
    val spritePokemon: String,
    val region: String,
    val type: String,
    val nationalCutoff: Int?,
)
