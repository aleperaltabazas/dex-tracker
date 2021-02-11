package com.github.aleperaltabazas.dex.dto.dex

data class LoginRequestDTO(
    val mail: String,
    val localDex: List<UserDexDTO>,
)
