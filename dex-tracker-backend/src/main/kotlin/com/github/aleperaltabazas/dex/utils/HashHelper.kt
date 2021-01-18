package com.github.aleperaltabazas.dex.utils

import org.apache.commons.codec.digest.DigestUtils

open class HashHelper {
    open fun sha256(s: String): String = DigestUtils.sha256Hex(s)
}