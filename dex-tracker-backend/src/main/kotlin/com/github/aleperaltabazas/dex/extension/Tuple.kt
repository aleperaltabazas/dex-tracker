package com.github.aleperaltabazas.dex.extension

fun <A, B> both(f: () -> A?, g: () -> B?): Pair<A, B>? {
    val a = f()
    val b = g()

    return if (a != null && b != null) {
        a to b
    } else {
        null
    }
}

fun <A, B, C> Pair<A, B>.fold(f: (A, B) -> C): C {
    val (a, b) = this

    return f(a, b)
}
