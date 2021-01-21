package com.github.aleperaltabazas.dex.utils

import java.text.SimpleDateFormat
import java.util.*

class IdGenerator(
    private val sdf: SimpleDateFormat
) {
    fun userId() = randomId("U")

    fun userDexId() = randomId("UD")

    private fun randomId(prefix: String): String = "$prefix-${sdf.format(Date())}-${UUID.randomUUID()}"
}