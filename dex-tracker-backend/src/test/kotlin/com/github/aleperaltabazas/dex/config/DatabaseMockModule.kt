package com.github.aleperaltabazas.dex.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.jetbrains.exposed.sql.Database

open class DatabaseMockModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("db")
    open fun db() = Database.connect("jdbc:h2:mem:regular", "org.h2.Driver")
}