package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.service.UsersService
import spark.ModelAndView
import spark.Request
import spark.Response
import spark.Spark.*
import spark.TemplateEngine

class FrontendController(
    private val templateEngine: TemplateEngine,
    private val usersService: UsersService,
) : Controller {
    override fun register() {
        get("/", this::home, templateEngine)
    }

    private fun home(req: Request, res: Response): ModelAndView {
        val dexToken = req.cookie(DEX_TOKEN)
        if (dexToken == null) {
            val (_, token) = usersService.createUser(null)
            res.cookie("/", DEX_TOKEN, token, 36000000, false)
        }

        return ModelAndView(emptyMap<String, Any?>(), "index.html")
    }
}