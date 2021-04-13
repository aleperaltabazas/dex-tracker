package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.dto.dex.CaughtStatusDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDexDTO
import com.github.aleperaltabazas.dex.extension.mapIf

data class User(
    val userId: String,
    val username: String? = null,
    val pokedex: List<UserDex>,
    val mail: String,
    val picture: String? = null,
) {
    fun owns(pokedexId: String) = pokedex.any { it.userDexId == pokedexId }

    fun updatePokedex(userDexId: String, status: List<CaughtStatusDTO>) = this.copy(
        pokedex = this.pokedex.mapIf({ it.userDexId == userDexId }) { it.updateStatus(status) }
    )

    fun mergePokedex(pokedex: List<UserDex>): User {
        val old = this.pokedex.map { pokedex.find { dex -> dex.userDexId == it.userDexId } ?: it }
        val new = pokedex.filter { old.none { dex -> dex.userDexId == it.userDexId } }

        return this.copy(
            pokedex = old + new
        )
    }

    fun update(changes: UpdateUserDTO): User = this.copy(
        username = changes.username ?: this.username,
    )
}

data class UserDex(
    val userDexId: String,
    val game: String,
    val type: PokedexType,
    val region: String,
    val name: String? = null,
    val pokemon: List<UserDexPokemon>
) {
    constructor(dto: UserDexDTO) : this(
        userDexId = dto.userDexId,
        game = dto.game.title,
        type = dto.type,
        region = dto.region,
        name = dto.name,
        pokemon = dto.pokemon,
    )

    fun updateStatus(status: List<CaughtStatusDTO>) = this.copy(
        pokemon = this.pokemon.map {
            it.copy(
                caught = status.find { s -> s.dexNumber == it.dexNumber }?.caught
                    ?: it.caught
            )
        }
    )

    fun caught(): Int = pokemon.count { it.caught }
}

data class UserDexPokemon(
    val name: String,
    val dexNumber: Int,
    val caught: Boolean,
)

data class Session(
    val token: String,
    val userId: String,
)
