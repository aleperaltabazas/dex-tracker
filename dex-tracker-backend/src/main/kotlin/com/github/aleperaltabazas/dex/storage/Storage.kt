package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoDatabase
import org.bson.Document

open class Storage(
    private val db: MongoDatabase,
    private val objectMapper: ObjectMapper,
) {
    open fun update(collection: Collection, filter: Document, value: Any) = db.getCollection(collection.collectionName)
        .updateOne(filter, convertToDocument(value))

    open fun insert(collection: Collection, value: Any) = db.getCollection(collection.collectionName)
        .insertOne(convertToDocument(value))

    open fun query(collection: Collection) = Query(
        coll = db.getCollection(collection.collectionName),
        objectMapper = objectMapper
    )

    open fun exists(collection: Collection, filter: Document) = count(collection, filter) > 0

    private fun count(collection: Collection, filter: Document): Int {
        return Query(
            coll = db.getCollection(collection.collectionName),
            objectMapper = objectMapper
        )
            .where(filter)
            .count()
    }

    private fun convertToDocument(v: Any) = Document(objectMapper.convertValue(v, MAP_REF))

    companion object {
        private val MAP_REF = object : TypeReference<Map<String, Any?>>() {}
    }
}