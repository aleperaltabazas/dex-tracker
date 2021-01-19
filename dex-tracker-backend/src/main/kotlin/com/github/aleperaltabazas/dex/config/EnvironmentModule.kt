package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.env.loadEnvironment
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class EnvironmentModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("env")
    fun env() = loadEnvironment()
}