package com.github.aleperaltabazas.dex.extension

import arrow.core.Either

fun <A, B> Either<A, Either<A, B>>.join(): Either<A, B> = when (this) {
    is Either.Left -> this
    is Either.Right -> this.value
}
