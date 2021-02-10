package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import org.bson.Document
import org.bson.conversions.Bson

open class Update(
    private val coll: MongoCollection<Document>,
    private val objectMapper: ObjectMapper
) {
    private var where: Document? = null
    private var updates: MutableList<Bson> = mutableListOf()

    open fun where(filter: Document): Update {
        this.where = filter
        return this
    }

    open fun set(key: String, value: Any?): Update {
        this.updates.add(Updates.set(key, convertToDocument(value)))
        return this
    }

    open fun add(key: String, value: Any?): Update {
        this.updates.add(Updates.addToSet(key, convertToDocument(value)))
        return this
    }

    open fun updateOne(): UpdateResult = coll
        .updateOne(where ?: Document(), Updates.combine(updates))

    open fun updateMany(): UpdateResult = coll
        .updateMany(where ?: Document(), Updates.combine(updates))

    private fun convertToDocument(v: Any?) = v?.let {
        Document(objectMapper.convertValue(it, MAP_REF))
    }

    companion object {
        private val MAP_REF = object : TypeReference<Map<String, Any?>>() {}
    }
}