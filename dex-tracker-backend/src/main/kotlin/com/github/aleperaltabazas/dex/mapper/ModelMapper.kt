package com.github.aleperaltabazas.dex.mapper

import com.github.aleperaltabazas.dex.dto.dex.GameDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDTO
import com.github.aleperaltabazas.dex.dto.dex.UserDexDTO
import com.github.aleperaltabazas.dex.model.User
import com.github.aleperaltabazas.dex.model.UserDex
import com.github.aleperaltabazas.dex.service.GameService

class ModelMapper(
    private val gameService: GameService
) {
    fun mapUserToDTO(user: User) = UserDTO(
        username = user.username,
        pokedex = user.pokedex.map { mapUserDexToDTO(it) }
    )

    fun mapUserDexToDTO(userDex: UserDex) = UserDexDTO(
        userDexId = userDex.userDexId,
        game = GameDTO(gameService.gameFromKey(userDex.game)),
        name = userDex.name,
    )
}
