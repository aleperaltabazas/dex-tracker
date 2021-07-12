package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.Favourite

data class UpdateUserDTO(
    val username: String? = null,
    val dex: Map<String, DexUpdateDTO>? = null,
    val favourites: List<Favourite>? = null,
)

data class DexUpdateDTO(
    val name: String? = null,
    val caught: List<Int>,
)

data class CreateDexDTO(
    val game: String,
    val name: String?,
)

data class SubscribeDTO(
    val userId: String,
    val dexId: String,
    val token: String?,
)
