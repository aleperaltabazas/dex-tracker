package com.github.aleperaltabazas.dex.db

import org.jetbrains.exposed.sql.Table

abstract class GenericTable<T>(tableName: String) : Table(tableName) {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)
}
