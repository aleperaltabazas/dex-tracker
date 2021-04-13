package com.github.aleperaltabazas.dex.mapper

import com.github.aleperaltabazas.dex.dto.dex.*
import com.github.aleperaltabazas.dex.model.GamePokedex
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.GameService

class ModelMapper(
    private val gameService: GameService
) {
    fun mapUserToDTO(user: User) = UserDTO(
        username = user.username,
        pokedex = user.pokedex.map { mapUserDexToDTO(it) },
        mail = user.mail,
        picture = user.picture,
    )

    fun mapUserDexToDTO(userDex: UserDex) = UserDexDTO(
        userDexId = userDex.userDexId,
        game = GameDTO(gameService.gameFromKey(userDex.game)),
        name = userDex.name,
        pokemon = userDex.pokemon,
        type = userDex.type,
        region = userDex.region,
        caught = userDex.caught(),
    )

    fun mapGamePokedexToDTO(dex: GamePokedex) = GamePokedexDTO(
        type = dex.type,
        game = GameDTO(dex.game),
        pokemon = dex.pokemon.mapIndexed { number, name -> DexEntryDTO(name = name, number = number) }
    )
}
