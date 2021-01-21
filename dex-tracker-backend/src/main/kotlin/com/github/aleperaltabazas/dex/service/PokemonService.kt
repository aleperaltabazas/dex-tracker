package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.GamePokedexCache
import com.github.aleperaltabazas.dex.dto.dex.DexEntryDTO
import com.github.aleperaltabazas.dex.dto.dex.FormDTO
import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.GamePokedexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.Pokemon
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import org.bson.Document

open class PokemonService(
    private val gamePokedexCache: GamePokedexCache,
    private val storage: Storage,
) {
    open fun allPokedex(): List<GamePokedexDTO> {
        val nationals = gamePokedexCache.get().filter { it.value.type == PokedexType.NATIONAL }
            .map { gameNationalPokedex(it.key) }
        val regionals = gamePokedexCache.get().filter { it.value.type == PokedexType.REGIONAL }
            .map { gameRegionalPokedex(it.key) }

        return regionals + nationals
    }

    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>) = gamePokedexCache.gameFromKey(gameKey)
        .let {
            val gen = it.game.gen
            numberOrName.fold(
                ifLeft = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("national_pokedex_number", it))
                        .limit(1)
                        .findAll(POKEMON_REF)
                        .firstOrNull()
                },
                ifRight = {
                    storage.query(Collection.POKEMON)
                        .where(Document("gen", gen).append("name", it))
                        .limit(1)
                        .findAll(POKEMON_REF)
                        .firstOrNull()
                },
            )
        } ?: throw NotFoundException("No pokemon found with ${numberOrName.fold({ "number $it" }, { "name $it" })}")

    open fun gameNationalPokedex(gameKey: String): GamePokedexDTO {
        val pokedex = gamePokedexCache.gameFromKey(gameKey)
        val pokemon = storage.query(Collection.POKEMON)
            .where(Document("gen", pokedex.game.gen))
            .findAll(POKEMON_REF)
            .map {
                DexEntryDTO(
                    name = it.name,
                    number = it.nationalPokedexNumber,
                    forms = it.forms.map { f -> FormDTO(f) }
                )
            }

        return GamePokedexDTO(
            pokemon = pokemon.toList(),
            type = PokedexType.NATIONAL,
            region = pokedex.game.region,
            game = GameDTO(pokedex.game)
        )
    }

    open fun gameRegionalPokedex(gameKey: String): GamePokedexDTO {
        val pokedex = gamePokedexCache.gameFromKey(gameKey)

        val pokemon = storage
            .query(Collection.POKEMON)
            .where(Document("gen", pokedex.game.gen))
            .findAll(POKEMON_REF)
            .filter { it.name in pokedex.pokemon }
            .map {
                DexEntryDTO(
                    number = pokedex.pokemon.indexOf(it.name),
                    name = it.name,
                    forms = it.forms.map { f -> FormDTO(f) }
                )
            }
            .toList()

        return GamePokedexDTO(
            pokemon = pokemon.toList(),
            type = PokedexType.REGIONAL,
            region = pokedex.game.region,
            game = GameDTO(pokedex.game)
        )
    }

    companion object {
        private val POKEMON_REF = object : TypeReference<Pokemon>() {}
    }
}