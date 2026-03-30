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
class DatabaseConfiguration(
    @Value("\${db.host}") private val host: String,
    @Value("\${db.name}") private val name: String,
    @Value("\${db.user}") private val user: String,
    @Value("\${db.password}") private val password: String,
) {
    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://$user:$password@$host/$name?authSource=admin")
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(settings)
    }

    @Bean
    fun mongoDatabase(mongoClient: MongoClient): MongoDatabase = mongoClient.getDatabase(name)
}
