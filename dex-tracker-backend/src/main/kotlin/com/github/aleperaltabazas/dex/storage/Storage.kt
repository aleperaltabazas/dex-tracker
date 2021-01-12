package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.DAO
import com.github.aleperaltabazas.dex.db.Where
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction

class Storage<A : LongEntity>(
    protected val db: Database,
    protected val table: DAO<A>,
) {
    fun findAll(where: Where) = transaction(db) {
        table.new {  }
        table.find(where).toList()
    }

    fun findAll(): List<A> = findAll { Op.TRUE }
}