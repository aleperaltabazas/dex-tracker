package com.github.aleperaltabazas.dex.extension

fun catching(f: () -> Unit): Throwable? = try {
    f()
    null
} catch (t: Throwable) {
    t
}
