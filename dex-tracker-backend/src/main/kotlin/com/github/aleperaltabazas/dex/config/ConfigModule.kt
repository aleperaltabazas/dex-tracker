package com.github.aleperaltabazas.dex.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

open class ConfigModule : AbstractModule() {
    @Provides
    @Singleton
    open fun config(): Config = ConfigFactory.load()
}