package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.HashHelper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named

class HashMockModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("hash")
    fun hash() = HashHelper()
}