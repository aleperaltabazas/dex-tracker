package com.github.aleperaltabazas.dex.db.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

data class Evolution(
    val id: Long?,
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
sealed class EvolutionMethod(val type: String) {
    companion object {
        fun parse(s: String): EvolutionMethod? = TODO()
    }

    fun serialize(): String = TODO()
}

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
