package com.github.aleperaltabazas.dex.extension

import arrow.core.Either
import arrow.core.extensions.fx
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.runBlocking

fun <R> Either.Companion.catchBlocking(f: suspend () -> R): Either<Throwable, R> = runBlocking { catch(f) }

fun <A> List<Either<Throwable, A>>.sequence(): Either<Throwable, List<A>> {
    if (this.isEmpty()) {
        return emptyList<A>().right()
    }

    return Either.fx {
        val a = this@sequence.first().bind()
        val `as` = this@sequence.drop(1).sequence().bind()

        return@fx listOf(a) + `as`
    }
}

fun <A, B> Either<A, Either<A, B>>.join(): Either<A, B> = when (this) {
    is Either.Left -> this.a.left()
    is Either.Right -> this.b
}
