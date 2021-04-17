package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.PokedexCache
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Game
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.bson.Document

open class PokedexService(
    private val pokedexCache: PokedexCache,
    private val storage: Storage,
    private val idGenerator: IdGenerator,
) {
    open fun allPokedex() = pokedexCache.get()

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

    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>): Pokemon = pokedexCache
        .pokedexOf(gameKey)
        .let {
            val gen = it.gen
            numberOrName.fold(
                ifLeft = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("national_pokedex_number", it))
                        .limit(1)
                        .findOne(POKEMON_REF)
                },
                ifRight = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("name", it))
                        .limit(1)
                        .findOne(POKEMON_REF)
                },
            )
        }
        ?: throw NotFoundException("No pokemon found with ${numberOrName.fold({ "number $it" }, { "name $it" })}")

    companion object {
        private val POKEMON_REF = object : TypeReference<Pokemon>() {}
    }
}