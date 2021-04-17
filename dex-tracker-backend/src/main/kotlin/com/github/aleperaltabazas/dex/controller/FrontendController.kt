package com.github.aleperaltabazas.dex.controller

import spark.ModelAndView
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.notFound
import spark.TemplateEngine

class FrontendController(
    private val engine: TemplateEngine,
) : Controller {
    override fun register() {
        get("", this::home, engine)
        get("/", this::home, engine)
        get("/dex/:id", this::home, engine)
        get("/users/:id", this::home, engine)
        notFound { _, _ ->
            engine.render(ModelAndView(null, "index.ftl"))
        }
    }

    private fun home(req: Request, res: Response) = ModelAndView(emptyMap<String, Any?>(), "index.ftl")
}