package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.UpdateResult
import org.bson.Document

open class Replace(
    private val coll: MongoCollection<Document>,
    private val objectMapper: ObjectMapper
) {
    private var where: Document? = null
    private var replacement: Document? = null

    open fun where(filter: Document): Replace {
        this.where = filter
        return this
    }

    open fun set(value: Any?): Replace {
        this.replacement = convertToDocument(value)
        return this
    }

    open fun replaceOne(): UpdateResult? = replacement?.let {
        coll.replaceOne(where ?: Document(), it)
    }

    private fun convertToDocument(v: Any?) = v?.let {
        Document(objectMapper.convertValue(it, MAP_REF))
    }

    companion object {
        private val MAP_REF = object : TypeReference<Map<String, Any?>>() {}
    }
}