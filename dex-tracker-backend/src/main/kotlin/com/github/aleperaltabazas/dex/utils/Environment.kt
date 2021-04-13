package com.github.aleperaltabazas.dex.utils

enum class Environment {
    DEV,
    LOCAL,
    TEST,
    PROD;

    fun isDev() = this in listOf(DEV, LOCAL)
}
