package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import com.github.aleperaltabazas.dex.utils.IdGenerator

open class PokedexService(
    private val pokedexCache: PokedexCache,
    private val idGenerator: IdGenerator,
) {
    open fun allPokedex() = pokedexCache.get()

    open fun pokedex(gameKey: String) = pokedexCache.pokedexOf(gameKey)

    open fun createUserDex(gameKey: String, name: String?): UserDex {
        val pokedex = pokedexCache.pokedexOf(gameKey)

        return UserDex(
            userDexId = idGenerator.userDexId(),
            game = Game(pokedex),
            region = pokedex.region,
            type = pokedex.type,
            pokemon = pokedex.entries.map {
                UserDexPokemon(
                    name = it.name,
                    dexNumber = it.number,
                    caught = false
                )
            },
            name = name,
            caught = 0,
        )
    }
}