package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bson.conversions.Bson

open class Query(
    private val coll: MongoCollection<Document>,
    private val objectMapper: ObjectMapper
) {
    private var where: Bson? = null
    private var sort: Document? = null
    private var limit: Int? = null
    private var offset: Int? = null

    open fun where(filter: Bson): Query {
        this.where = filter
        return this
    }

    open fun limit(value: Int): Query {
        this.limit = value
        return this
    }

    open fun offset(value: Int): Query {
        this.offset = value
        return this
    }

    open fun sort(by: Document): Query {
        this.sort = by
        return this
    }

    open fun count(): Int = coll.count(where ?: Document()).toInt()

    open fun <T> findOne(`as`: TypeReference<T>): T? = createMongoQuery()
        .limit(1)
        .map { objectMapper.convertValue(it, `as`) }
        .firstOrNull()

    open fun <T> findAll(`as`: TypeReference<T>?): List<T> = createMongoQuery()
        .map { objectMapper.convertValue(it, `as`) }
        .toList()

    private fun createMongoQuery() = coll.find(where ?: Document())
        .let { sort?.let(it::sort) ?: it }
        .let { limit?.let(it::limit) ?: it }
        .let { offset?.let(it::skip) ?: it }
}