package com.github.aleperaltabazas.dex.model

import com.github.aleperaltabazas.dex.dto.dex.DexUpdateDTO
import com.github.aleperaltabazas.dex.dto.dex.UpdateUserDTO
import com.github.aleperaltabazas.dex.extension.find

data class User(
    val userId: String,
    val username: String? = null,
    val pokedex: List<UserDex>,
    val mail: String,
    val picture: String? = null,
) {
    fun owns(pokedexId: String) = pokedex.any { it.userDexId == pokedexId }

    fun mergePokedex(pokedex: List<UserDex>): User {
        val old = this.pokedex.map { pokedex.find { dex -> dex.userDexId == it.userDexId } ?: it }
        val new = pokedex.filter { old.none { dex -> dex.userDexId == it.userDexId } }

        return this.copy(
            pokedex = old + new
        )
    }

    fun update(changes: UpdateUserDTO): User = this.copy(
        username = changes.username ?: this.username,
        pokedex = pokedex.map { dex ->
            changes.dex
                ?.find { dexId, _ -> dexId == dex.userDexId }
                ?.let { (_, u) -> dex.update(u) } ?: dex
        },
    )

    fun addDex(dex: UserDex) = this.copy(
        pokedex = pokedex + dex,
    )
}

data class UserDex(
    val userDexId: String,
    val game: Game,
    val type: PokedexType,
    val region: String,
    val name: String? = null,
    val pokemon: List<UserDexPokemon>
) {
    fun caught(): Int = pokemon.count { it.caught }

    fun update(changes: DexUpdateDTO) = this.copy(
        name = changes.name ?: this.name,
        pokemon = this.pokemon.map { p -> p.copy(caught = p.dexNumber in changes.caught) }
    )
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
