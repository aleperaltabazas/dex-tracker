package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.Environment
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config

class EnvironmentModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("env")
    fun environment(config: Config) = Environment.valueOf(config.getString("env").toUpperCase())
}