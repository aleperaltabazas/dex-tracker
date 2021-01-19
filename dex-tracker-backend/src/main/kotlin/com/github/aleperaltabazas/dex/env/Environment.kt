package com.github.aleperaltabazas.dex.env

import com.typesafe.config.ConfigFactory

enum class Env {
    DEV,
    PROD,
    TEST,
}

lateinit var environment: Env
    private set

fun loadEnvironment(): Env {
    val config = ConfigFactory.load()
    environment = Env.valueOf(config.getString("env")?.toUpperCase()
        ?: throw IllegalStateException("Unknown environment"))
    return environment
}
