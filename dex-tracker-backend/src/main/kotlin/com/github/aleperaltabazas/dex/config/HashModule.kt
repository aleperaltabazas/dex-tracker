package com.github.aleperaltabazas.dex.config

import com.github.aleperaltabazas.dex.utils.HashHelper
import com.github.aleperaltabazas.dex.utils.IdGenerator
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import java.text.SimpleDateFormat

class HashModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("hash")
    fun hash() = HashHelper()

    @Provides
    @Singleton
    @Named("idGenerator")
    fun idGenerator() = IdGenerator(
        SimpleDateFormat("yyyy-MM-dd")
    )
}