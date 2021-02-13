package com.github.aleperaltabazas.dex.mapper

import com.github.aleperaltabazas.dex.dto.dex.*
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.GameService

class ModelMapper(
    private val gameService: GameService
) {
    fun mapUserToDTO(user: User) = UserDTO(
        username = user.username,
        pokedex = user.pokedex.map { mapToRefDTO(it) },
        mail = user.mail,
    )

    fun mapUserDexToDTO(userDex: UserDex) = UserDexDTO(
        userDexId = userDex.userDexId,
        game = GameDTO(gameService.gameFromKey(userDex.game)),
        name = userDex.name,
        pokemon = userDex.pokemon,
        type = userDex.type,
        region = userDex.region
    )

    fun mapToRefDTO(userDex: UserDex) = UserDexRefDTO(
        userDexId = userDex.userDexId,
        game = GameDTO(gameService.gameFromKey(userDex.game)),
        name = userDex.name,
        caught = userDex.pokemon.count { it.caught },
    )

    fun mapToLoginResponseDTO(user: User) = LoginResponseDTO(
        username = user.username,
        mail = user.mail,
        pokedex = user.pokedex.map { mapUserDexToDTO(it) }
    )
}
