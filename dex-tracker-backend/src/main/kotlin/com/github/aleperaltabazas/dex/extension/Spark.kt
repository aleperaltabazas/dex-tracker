package com.github.aleperaltabazas.dex.extension

import spark.Request

fun Request.prettyHeaders(): String = headers().joinToString(",") { "\"$it:${headers(it)}\"" }
