package com.github.aleperaltabazas.dex.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class FrontendController {
    @RequestMapping(value = ["/{path:[^\\.]*}", "/{path:^(?!api).*}/{subpath:[^\\.]*}"])
    fun forward(): String = "forward:/index.html"
}
