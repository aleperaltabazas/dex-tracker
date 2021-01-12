package com.github.aleperaltabazas.dex.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.aleperaltabazas.dex.db.dao.EvolutionDAO

data class Evolution(
    val name: String,
    val method: EvolutionMethod,
) {
    constructor(dao: EvolutionDAO) : this(
        name = dao.name,
        method = dao.method,
    )
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = LevelUp::class, name = "LEVEL_UP"),
    JsonSubTypes.Type(value = UseItem::class, name = "USE_ITEM"),
    JsonSubTypes.Type(value = Trade::class, name = "TRADE"),
)
sealed class EvolutionMethod(val type: String)

data class LevelUp(
    val level: Int?,
    val friendship: Int?,
    val move: String?,
    val location: String?,
    val time: String?,
    val item: String?,
    val gender: String?,
    val upsideDown: Boolean?,
    val region: String?,
) : EvolutionMethod(type = "LEVEL_UP")

data class UseItem(
    val item: String,
    val gender: String?,
    val region: String?,
) : EvolutionMethod(type = "USE_ITEM")

data class Trade(
    val item: String?,
    val pokemon: String?,
) : EvolutionMethod(type = "TRADE")
