package com.github.aleperaltabazas.dex.dto.dex

data class UpdateUserDTO(
    val username: String? = null,
    val dex: Map<String, DexUpdateDTO>? = null,
)

data class DexUpdateDTO(
    val name: String? = null,
    val caught: List<Int>,
    val favourites: List<Int>,
)

data class CreateDexDTO(
    val game: String,
    val name: String?,
)
