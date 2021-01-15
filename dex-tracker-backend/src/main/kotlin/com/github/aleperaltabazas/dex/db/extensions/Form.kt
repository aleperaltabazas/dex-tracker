package com.github.aleperaltabazas.dex.db.extensions

import com.github.aleperaltabazas.dex.db.schema.FormsTable
import com.github.aleperaltabazas.dex.model.Form
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toForm(): Form = Form(
    name = this[FormsTable.name]
)
