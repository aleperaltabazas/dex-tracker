package com.github.aleperaltabazas.dex.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

data class Evolution(
    val name: String,
    val method: EvolutionMethod,
)

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
    val level: Int? = null,
    val friendship: Int? = null,
    val move: String? = null,
    val location: String? = null,
    val time: String? = null,
    val item: String? = null,
    val gender: String? = null,
    val upsideDown: Boolean? = null,
    val region: String? = null,
) : EvolutionMethod(type = "LEVEL_UP")

data class UseItem(
    val item: String,
    val gender: String? = null,
    val region: String? = null,
) : EvolutionMethod(type = "USE_ITEM")

data class Trade(
    val item: String? = null,
    val pokemon: String? = null,
) : EvolutionMethod(type = "TRADE")
