package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.model.Form
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert

fun ResultRow.toForm(): Form = Form(
    name = this[FormsTable.name]
)

fun FormsTable.insert(forms: List<Form>, pokemonId: Long) = batchInsert(forms) { f ->
    this[name] = f.name
    this[FormsTable.pokemonId] = pokemonId
}
