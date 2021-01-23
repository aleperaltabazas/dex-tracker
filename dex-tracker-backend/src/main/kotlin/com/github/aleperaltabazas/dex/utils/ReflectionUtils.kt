package com.github.aleperaltabazas.dex.utils

import org.apache.commons.lang3.reflect.FieldUtils

object ReflectionUtils {
    fun getField(obj: Any, fieldName: String): Any {
        return try {
            val field = FieldUtils.getField(obj.javaClass, fieldName, true)
            field[obj]
        } catch (e: Exception) {
            throw RuntimeException("cannot get handlebars", e)
        }
    }
}