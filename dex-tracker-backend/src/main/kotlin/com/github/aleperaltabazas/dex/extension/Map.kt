package com.github.aleperaltabazas.dex.extension

fun <A, B> Map<A, B>.find(f: (A, B) -> Boolean): Pair<A, B>? {
    for ((k, v) in this) {
        if (f(k, v)) {
            return k to v
        }
    }

    return null
}
