package com.github.aleperaltabazas.dex.controller

import com.github.aleperaltabazas.dex.constants.DEX_TOKEN
import com.github.aleperaltabazas.dex.service.UsersService
import org.slf4j.LoggerFactory
import spark.ModelAndView
import spark.Request
import spark.Response
import spark.Spark.after
import spark.Spark.get
import spark.TemplateEngine

class FrontendController(
    private val templateEngine: TemplateEngine,
    private val usersService: UsersService,
) : Controller {
    override fun register() {
        get("/", this::home, templateEngine)
        get("/dex/:id", this::home, templateEngine)
    }

    private fun home(req: Request, res: Response): ModelAndView {
        val dexToken = req.cookie(DEX_TOKEN)
        if (dexToken == null) {
            val (_, token) = usersService.createUser(null)
            res.cookie("/", DEX_TOKEN, token, 36000000, false)
        }

        return ModelAndView(emptyMap<String, Any?>(), "index.html")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FrontendController::class.java)
    }
}