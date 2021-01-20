package com.github.aleperaltabazas.dex.db.fixture

import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon

val andrew = User(
    username = "andrew",
    pokedex = emptyList(),
)

val brenda = User(
    username = "brenda",
    pokedex = emptyList(),
)

val charles = User(
    username = "charles",
    pokedex = emptyList(),
)

val daniel = User(
    username = "daniel",
    pokedex = listOf(
        UserDex(
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = emptyList(),
        )
    )
)

val emily = User(
    username = "emily",
    pokedex = listOf(
        UserDex(
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = listOf(
                UserDexPokemon(
                    name = "bulbasaur",
                    dexNumber = 1,
                    caught = false
                )
            )
        )
    )
)

val frank = User(
    username = "frank",
    pokedex = listOf(
        UserDex(
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = listOf(
                UserDexPokemon(
                    name = "bulbasaur",
                    dexNumber = 1,
                    caught = true
                ),
                UserDexPokemon(
                    name = "ivysaur",
                    dexNumber = 2,
                    caught = false
                ),
                UserDexPokemon(
                    name = "venusaur",
                    dexNumber = 1,
                    caught = true
                ),
            )
        ),
        UserDex(
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = listOf(
                UserDexPokemon(
                    name = "charmeleon",
                    dexNumber = 5,
                    caught = true
                ),
            )
        ),
        UserDex(
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = listOf(
                UserDexPokemon(
                    name = "charizard",
                    dexNumber = 6,
                    caught = false
                ),
            )
        )
    )
)

val richard = User(
    username = "richard",
    pokedex = emptyList(),
)
