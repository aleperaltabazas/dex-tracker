package com.github.aleperaltabazas.dex.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.typesafe.config.Config

class DatabaseModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("mongoClient")
    fun mongoClient(config: Config): MongoClient {
        val user = System.getenv("DATABASE_USERNAME") ?: config.getString("db.user")
        val password = System.getenv("DATABASE_PASSWORD") ?: config.getString("db.password")
        val host = config.getString("db.host")
        val database = config.getString("db.name")
        val srv = config.hasPath("db.srv") && config.getBoolean("db.srv")
        val string = "mongodb${if (srv) "+srv" else ""}://"

        val connectionString = ConnectionString("$string$user:$password@$host/$database")

        val credential = MongoCredential.createCredential(user, database, password.toCharArray())

        val mongoClientSettings = MongoClientSettings.builder()
            .credential(credential)
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    @Provides
    @Singleton
    @Named("db")
    fun db(
        @Named("mongoClient") mongoClient: MongoClient,
        config: Config,
    ): MongoDatabase = mongoClient.getDatabase(config.getString("db.name"))
}