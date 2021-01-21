package com.github.aleperaltabazas.dex.extension

fun <T> List<T>.mapIf(condition: (T) -> Boolean, mapping: (T) -> T): List<T> = this.map {
    if (condition(it)) mapping(it) else it
}
