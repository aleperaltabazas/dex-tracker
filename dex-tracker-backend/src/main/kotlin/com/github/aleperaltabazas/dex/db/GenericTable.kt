package com.github.aleperaltabazas.dex.db

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

abstract class GenericTable<T>(tableName: String) : Table(tableName) {
    val id = long("id").autoIncrement()

    abstract fun reify(row: ResultRow): T

    override val primaryKey = PrimaryKey(id)
}

infix fun <T> GenericTable<T>.reifying(f: GenericTable<T>.() -> Query): Sequence<T> = f().asSequence().map { reify(it) }
