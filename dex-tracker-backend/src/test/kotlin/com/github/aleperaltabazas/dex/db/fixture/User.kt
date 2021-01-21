package com.github.aleperaltabazas.dex.db.fixture

import com.github.aleperaltabazas.dex.model.PokedexType
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.model.UserDexPokemon

val andrew = User(
    userId = "U-1",
    username = "andrew",
    pokedex = emptyList(),
)

val brenda = User(
    userId = "U-2",
    username = "brenda",
    pokedex = emptyList(),
)

val charles = User(
    userId = "U-3",
    username = "charles",
    pokedex = emptyList(),
)

val daniel = User(
    userId = "U-4",
    username = "daniel",
    pokedex = listOf(
        UserDex(
            userDexId = "UD-1",
            game = "hgss",
            type = PokedexType.NATIONAL,
            region = "johto",
            pokemon = emptyList(),
        )
    )
)

val emily = User(
    userId = "U-5",
    username = "emily",
    pokedex = listOf(
        UserDex(
            userDexId = "UD-2",
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
    userId = "U-6",
    username = "frank",
    pokedex = listOf(
        UserDex(
            userDexId = "UD-3",
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
            userDexId = "UD-4",
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
            userDexId = "UD-5",
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
    userId = "U-7",
    username = "richard",
    pokedex = emptyList(),
)
