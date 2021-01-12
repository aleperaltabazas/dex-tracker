package com.github.aleperaltabazas.dex.dto.dex

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

data class EvolutionDTO(
    val name: String,
    val method: EvolutionMethodDTO,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = LevelUpDTO::class, name = "LEVEL_UP"),
    JsonSubTypes.Type(value = UseItemDTO::class, name = "USE_ITEM"),
    JsonSubTypes.Type(value = TradeDTO::class, name = "TRADE"),
)
sealed class EvolutionMethodDTO(val type: String)

data class LevelUpDTO(
    val level: Int?,
    val friendship: Int?,
    val move: String?,
    val location: String?,
    val time: String?,
    val item: String?,
    val gender: String?,
    val upsideDown: Boolean?,
    val region: String?,
) : EvolutionMethodDTO(type = "LEVEL_UP")

data class UseItemDTO(
    val item: String,
    val gender: String?,
    val region: String?,
) : EvolutionMethodDTO(type = "USE_ITEM")

data class TradeDTO(
    val item: String?,
    val pokemon: String?,
) : EvolutionMethodDTO(type = "TRADE")
