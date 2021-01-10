package com.github.aleperaltabazas.dex.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("db")
    fun db(config: Config): Database {
        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = config.getString("db.connection-string")
            this.driverClassName = config.getString("db.driver")
            this.username = config.getString("db.user")
            this.password = config.getString("db.password")
            this.maximumPoolSize = config.getInt("db.hikari.pool-size")
        }

        val source = HikariDataSource(hikariConfig)

        return Database.connect(source)
    }
}