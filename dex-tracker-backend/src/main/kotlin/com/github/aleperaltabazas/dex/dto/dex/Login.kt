package com.github.aleperaltabazas.dex.dto.dex

import com.github.aleperaltabazas.dex.model.UserDex

data class LoginRequestDTO(
    val mail: String,
    val googleToken: String,
)
