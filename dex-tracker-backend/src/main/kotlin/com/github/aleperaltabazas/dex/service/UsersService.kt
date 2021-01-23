package com.github.aleperaltabazas.dex.service

import com.fasterxml.jackson.core.type.TypeReference
import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.dto.dex.CreateUserDexDTO
import com.github.aleperaltabazas.dex.exception.BadRequestException
import com.github.aleperaltabazas.dex.exception.ForbiddenException
import com.github.aleperaltabazas.dex.exception.NotFoundException
import com.github.aleperaltabazas.dex.model.*
import com.github.aleperaltabazas.dex.storage.Collection
import com.github.aleperaltabazas.dex.storage.Storage
import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import org.bson.Document

class UsersService(
    private val storage: Storage,
    private val pokemonService: PokemonService,
    private val idGenerator: IdGenerator,
    private val hash: HashHelper,
) {
    fun createUserDex(token: String, dexRequest: CreateUserDexDTO): UserDex {
        val user = findUser(token)

        val pokedex = if (dexRequest.type == PokedexType.NATIONAL) {
            pokemonService.gameNationalPokedex(dexRequest.game)
        } else {
            pokemonService.gameRegionalPokedex(dexRequest.game)
        }

        val dexId = idGenerator.userDexId()

        val userDex = UserDex(
            userDexId = dexId,
            game = pokedex.game.title,
            region = pokedex.region,
            type = pokedex.type,
            pokemon = pokedex.pokemon.map {
                UserDexPokemon(
                    name = it.name,
                    dexNumber = it.number,
                    caught = false
                )
            },
            name = dexRequest.name
        )

        storage.update(Collection.USERS)
            .where(Document("user_id", user.userId))
            .add("pokedex", userDex)
            .updateOne()

        return userDex
    }


    fun findUser(token: String) = storage.query(Collection.SESSIONS)
        .where(Document("token", token))
        .findOne(SESSION_REF)
        ?.let {
            storage.query(Collection.USERS)
                .where(Document("user_id", it.userId))
                .findOne(USER_REF)
        } ?: throw NotFoundException("No user found for session token $token")

    fun createUser(username: String?): Pair<User, String> {
        if (username != null && storage.exists(Collection.USERS, Document("username", username))) {
            throw BadRequestException("Username $username is already in use")
        }

        val userId = idGenerator.userId()

        val user = User(
            userId = userId,
            username = username,
            pokedex = emptyList(),
        )

        storage.insert(Collection.USERS, user)

        return user to createSession(userId)
    }

    fun findUserDex(token: String, dexId: String): UserDex = findUser(token).let { user ->
        user.pokedex
            .find { it.userDexId == dexId }
            ?: throw NotFoundException("User dex with id $dexId not found for user ${user.userId}")
    }

    private fun createSession(userId: String): String {
        val token = hash.sha256(userId)

        storage.insert(Collection.SESSIONS, Session(token = token, userId = userId))
        return token
    }

    fun updateCaughtStatus(token: String, status: List<CaughtStatusDTO>) {
        val user = this.findUser(token)

        for (s in status) {
            if (!user.owns(s.pokedexId)) {
                throw ForbiddenException("User is not allowed to edit pokedex by ${s.pokedexId}")
            }
        }

        val dexToUpdate = status.groupBy {
            it.pokedexId
        }

        storage
            .replace(collection = Collection.USERS)
            .where(Document("user_id", user.userId))
            .set(
                value = user.copy(
                    pokedex = dexToUpdate.toList().fold(user) { u, (dexId, status) ->
                        u.updatePokedex(
                            dexId,
                            status
                        )
                    }.pokedex
                )
            )
            .replaceOne()
    }

    companion object {
        private val SESSION_REF = object : TypeReference<Session>() {}
        private val USER_REF = object : TypeReference<User>() {}
    }
}