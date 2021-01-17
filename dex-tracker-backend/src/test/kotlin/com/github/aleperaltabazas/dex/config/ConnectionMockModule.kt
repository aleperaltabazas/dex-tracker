package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.connector.RestConnector
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.nhaarman.mockito_kotlin.mock

open class ConnectionMockModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("pokeapiConnector")
    open fun pokeapiConnector(): RestConnector = mock {}
}