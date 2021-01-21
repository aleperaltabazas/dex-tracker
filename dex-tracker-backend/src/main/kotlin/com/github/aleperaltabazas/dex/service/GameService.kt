package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game

open class GameService(
    private val games: List<Game>
) {
    open fun all(): List<Game> = games

    open fun gameFromKey(key: String) = when {
        key.matches("\\d+".toRegex()) -> games.find { it.gen == key.toInt() }
            ?: throw NotFoundException("No game found for generation $key")
        else -> games.find { it.title == key || it.region == key }
            ?: throw NotFoundException("No game found for string key $key")
    }
}