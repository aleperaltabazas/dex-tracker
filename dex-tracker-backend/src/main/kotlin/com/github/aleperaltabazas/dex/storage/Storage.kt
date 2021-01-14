package com.github.aleperaltabazas.dex.storage

import com.github.aleperaltabazas.dex.db.DAO
import com.github.aleperaltabazas.dex.db.Where
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Storage<E : LongEntity, M>(
    protected val db: Database,
    protected val table: DAO<E>,
) {
    fun findAll(where: Where): List<M> = transaction(db) {
        table.find(where).map { toModel(it) }
    }

    fun findAll(): List<M> = findAll { Op.TRUE }

    abstract fun save(m: M): E

    abstract fun toModel(dao: E): M
}