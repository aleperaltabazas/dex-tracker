package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.bson.Document
import org.springframework.stereotype.Service

@Service
open class PokemonService(
    private val pokedexService: PokedexService,
    private val storage: Storage,
) {
    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>): Pokemon = pokedexService
        .pokedex(gameKey)
        .let { pokedex ->
            val gen = pokedex.gen
            numberOrName.fold(
                ifLeft = { num ->
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("national_pokedex_number", num))
                        .limit(1)
                        .findOne(POKEMON_REF)
                },
                ifRight = { name ->
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("name", name))
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
