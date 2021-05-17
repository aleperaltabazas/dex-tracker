package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.bson.Document

open class PokemonService(
    private val pokedexService: PokedexService,
    private val storage: Storage,
) {
    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>): Pokemon = pokedexService
        .pokedex(gameKey)
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