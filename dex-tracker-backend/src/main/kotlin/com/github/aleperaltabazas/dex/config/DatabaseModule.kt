package com.github.aleperaltabazas.dex.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DatabaseModule {
    @Bean
    open fun mongoClient(
        @Value("\${app.db.host}") host: String,
        @Value("\${app.db.name}") database: String,
        @Value("\${app.db.srv:false}") srv: Boolean,
    ): MongoClient {
        val user = System.getenv("DATABASE_USERNAME") ?: ""
        val password = System.getenv("DATABASE_PASSWORD") ?: ""
        val scheme = "mongodb${if (srv) "+srv" else ""}://"

        val connectionString = if (user.isNotBlank() && password.isNotBlank()) {
            ConnectionString("$scheme$user:$password@$host/$database")
        } else {
            ConnectionString("$scheme$host/$database")
        }

        val settingsBuilder = MongoClientSettings.builder()
            .applyConnectionString(connectionString)

        if (user.isNotBlank() && password.isNotBlank()) {
            settingsBuilder.credential(
                MongoCredential.createCredential(user, database, password.toCharArray())
            )
        }

        return MongoClients.create(settingsBuilder.build())
    }

    @Bean
    open fun db(
        mongoClient: MongoClient,
        @Value("\${app.db.name}") name: String,
    ): MongoDatabase = mongoClient.getDatabase(name)
}