package com.github.aleperaltabazas.dex.storage

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bson.conversions.Bson

open class Delete(
    private val coll: MongoCollection<Document>,
) {
    private var where: Bson? = null

    open fun where(filter: Bson): Delete {
        this.where = filter
        return this
    }

    open fun deleteOne() {
        coll.deleteOne(where ?: Document())
    }

    open fun deleteMany() {
        coll.deleteMany(where ?: Document())
    }
}