package com.github.aleperaltabazas.dex.service

import com.github.aleperaltabazas.dex.db.schema.UsersTable
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon
import com.github.aleperaltabazas.dex.storage.UsersStorage

class UsersService(
    private val usersStorage: UsersStorage,
    private val pokemonService: PokemonService,
) {
    fun createUserDex(token: String, game: String, type: PokedexType): UserDex {
        val user = findUser(token)

        val pokedex = if (type == PokedexType.NATIONAL) {
            pokemonService.gameNationalPokedex(game)
        } else {
            pokemonService.gameRegionalPokedex(game)
        }

        val userDex = UserDex(
            game = pokedex.game.title,
            region = pokedex.region,
            type = pokedex.type,
            pokemon = pokedex.pokemon.map {
                UserDexPokemon(
                    name = it.name,
                    dexNumber = it.number,
                    caught = false
                )
            }
        )

        return usersStorage.createUserDex(
            userId = user.id!!,
            userDex,
        )
    }

    fun findUser(token: String) = usersStorage.findByToken(token)

    fun createUser(username: String?): Pair<User, String> {
        if (username != null && usersStorage.exists { UsersTable.username eq username }) {
            throw BadRequestException("Username $username is already in use")
        }

        return usersStorage.createUser()
    }

    fun updateCaughtStatus(token: String, status: List<CaughtStatusDTO>) {
        val user = usersStorage.findByToken(token)

        for (s in status) {
            if (!user.owns(s.pokedexId)) {
                throw ForbiddenException("User is not allowed to edit pokedex by ${s.pokedexId}")
            }
        }

        status.forEach { s ->
            usersStorage.updateCaughtStatus(
                pokedexId = s.pokedexId,
                dexNumber = s.dexNumber,
                caught = s.caught,
            )
        }
    }
}