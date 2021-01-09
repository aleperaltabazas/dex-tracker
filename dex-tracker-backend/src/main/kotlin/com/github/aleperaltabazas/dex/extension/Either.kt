package com.github.aleperaltabazas.dex.extension

import arrow.core.Either
import kotlinx.coroutines.runBlocking

fun <R> Either.Companion.catchBlocking(f: suspend () -> R): Either<Throwable, R> = runBlocking { Either.catch(f) }
