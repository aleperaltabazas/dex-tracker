package com.github.aleperaltabazas.dex.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import org.jetbrains.exposed.sql.Database

class DatabaseModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("db")
    fun db(config: Config): Database {
        val user = config.getString("db.user")
        val password = config.getString("db.password")
        val connectionString = config.getString("db.connection-string")
        val driver = config.getString("db.driver")

        return Database.connect(connectionString, driver = driver, user = user, password = password)
    }
}