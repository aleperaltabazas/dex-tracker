package com.github.aleperaltabazas.dex.service

import arrow.core.Either
import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.cache.pokedex.RegionalPokedexCache
import com.github.aleperaltabazas.dex.dto.dex.CreateUserDexDTO
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.*
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.ascending
import org.bson.Document

open class PokedexService(
    private val gameService: GameService,
    private val regionalPokedexCache: RegionalPokedexCache,
    private val storage: Storage,
    private val idGenerator: IdGenerator,
) {
    open fun createUserDex(dexRequest: CreateUserDexDTO): UserDex {
        val pokedex = if (dexRequest.type == PokedexType.NATIONAL) {
            this.gameNationalPokedex(dexRequest.game)
        } else {
            this.gameRegionalPokedex(dexRequest.game)
        }

        val dexId = idGenerator.userDexId()

        return UserDex(
            userDexId = dexId,
            game = pokedex.game.title,
            region = pokedex.game.region,
            type = pokedex.type,
            pokemon = pokedex.pokemon.mapIndexed { number, name ->
                UserDexPokemon(
                    name = name,
                    dexNumber = number,
                    caught = false
                )
            },
            name = dexRequest.name,
        )
    }

    open fun allPokedex(): List<GamePokedex> {
        val nationals = gameService.all().map { gameNationalPokedex(it) }
        val regionals = gameService.all().map { gameRegionalPokedex(it) }

        return regionals + nationals
    }

    open fun pokemon(gameKey: String, numberOrName: Either<Int, String>): Pokemon = gameService.gameFromKey(gameKey)
        .let { regionalPokedexCache.pokedexOf(it) }
        .let { dex ->
            val gen = dex.game.gen
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
        } ?: throw NotFoundException("No pokemon found with ${numberOrName.fold({ "number $it" }, { "name $it" })}")

    open fun gameNationalPokedex(gameKey: String): GamePokedex = gameNationalPokedex(
        game = gameService.gameFromKey(gameKey)
    )

    open fun gameRegionalPokedex(gameKey: String): GamePokedex = gameRegionalPokedex(
        game = gameService.gameFromKey(gameKey)
    )

    private fun gameNationalPokedex(game: Game): GamePokedex {
        val pokemon = storage.query(Collection.POKEMON)
            .where(Document("gen", game.gen))
            .sort(ascending("national_pokedex_number"))
            .findAll(POKEMON_REF)
            .map { it.name }

        return GamePokedex(
            game = game,
            type = PokedexType.NATIONAL,
            pokemon = pokemon,
        )
    }

    private fun gameRegionalPokedex(game: Game): GamePokedex {
        val pokedex = regionalPokedexCache.pokedexOf(game)

        val pokemon = storage
            .query(Collection.POKEMON)
            .where(Document("gen", game.gen))
            .where(Filters.`in`("name", pokedex.pokemon))
            .findAll(POKEMON_REF)
            .asSequence()
            .map { p ->
                pokedex.pokemon.indexOf(p.name) + 1 to p.name
            }
            .sortedBy { (number, _) -> number }
            .map { (_, name) -> name }
            .toList()

        return GamePokedex(
            pokemon = pokemon.toList(),
            type = PokedexType.REGIONAL,
            game = game,
        )
    }

    companion object {
        private val POKEMON_REF = object : TypeReference<Pokemon>() {}
    }
}