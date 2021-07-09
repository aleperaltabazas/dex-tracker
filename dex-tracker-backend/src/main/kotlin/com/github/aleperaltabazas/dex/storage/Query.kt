package com.github.aleperaltabazas.dex.storage

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.conversions.Bson

open class Query(
    private val coll: MongoCollection<Document>,
    private val objectMapper: ObjectMapper
) {
    private var where: Bson? = null
    private val operations: MutableList<Bson> = mutableListOf()
    private var sort: Bson? = null
    private var limit: Int? = null
    private var offset: Int? = null

    open fun where(filter: Bson): Query {
        this.where = Filters.and(where ?: Document(), filter)
        operations.add(Aggregates.match(filter))
        return this
    }

    open fun limit(value: Int): Query {
        this.limit = value
        operations.add(Aggregates.limit(value))
        return this
    }

    open fun offset(value: Int): Query {
        this.offset = value
        operations.add(Aggregates.skip(value))
        return this
    }

    open fun join(collection: Collection, localField: String, foreignField: String, `as`: String): Query {
        operations.add(
            Aggregates.lookup(
                collection.collectionName,
                localField,
                foreignField,
                `as`,
            )
        )
        return this
    }

    open fun sort(by: Bson): Query {
        this.sort = by
        operations.add(Aggregates.sort(by))
        return this
    }

    open fun count(): Int = coll.count(where ?: Document()).toInt()

    open fun <T> findOne(`as`: TypeReference<T>?): T? = createMongoQuery()
        .limit(1)
        .map { objectMapper.convertValue(it, `as`) }
        .firstOrNull()

    open fun <T> findOneAggregated(`as` : TypeReference<T>?): T? = coll
        .aggregate(operations)
        .map { objectMapper.convertValue(it, `as`) }
        .firstOrNull()

    open fun <T> findAll(`as`: TypeReference<T>?): List<T> = createMongoQuery()
        .map { objectMapper.convertValue(it, `as`) }
        .toList()

    private fun createMongoQuery() = coll
        .find(where ?: Document())
        .let { sort?.let(it::sort) ?: it }
        .let { limit?.let(it::limit) ?: it }
        .let { offset?.let(it::skip) ?: it }
}