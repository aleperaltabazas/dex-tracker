package com.github.aleperaltabazas.dex.utils

import java.text.SimpleDateFormat
import java.util.*

open class IdGenerator(
    private val sdf: SimpleDateFormat
) {
    open fun userId() = randomId("U")

    open fun userDexId() = randomId("UD")

    private fun randomId(prefix: String): String = "$prefix-${sdf.format(Date())}-${UUID.randomUUID()}"
}