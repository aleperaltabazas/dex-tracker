package com.github.aleperaltabazas.dex.utils

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Helper
import com.github.jknack.handlebars.HumanizeHelper
import com.github.jknack.handlebars.helper.I18nHelper
import com.github.jknack.handlebars.helper.NumberHelper
import com.github.jknack.handlebars.helper.StringHelpers
import spark.template.handlebars.HandlebarsTemplateEngine

class HandlebarsTemplateEngineBuilder(private val engine: HandlebarsTemplateEngine) {
    fun withHelper(name: String, helper: Helper<*>): HandlebarsTemplateEngineBuilder {
        handlerbars.registerHelper(name, helper)
        return this
    }

    private val handlerbars: Handlebars
        get() = ReflectionUtils.getField(engine, "handlebars") as Handlebars

    fun build(): HandlebarsTemplateEngine = engine

    fun withDefaultHelpers(): HandlebarsTemplateEngineBuilder {
        StringHelpers.register(handlerbars)
        NumberHelper.register(handlerbars)
        HumanizeHelper.register(handlerbars)
        withHelper("i18n", I18nHelper.i18n)
        return this
    }

    companion object {
        fun create(): HandlebarsTemplateEngineBuilder = HandlebarsTemplateEngineBuilder(HandlebarsTemplateEngine())
    }
}