package com.github.aleperaltabazas.dex.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class FrontendController {
    @GetMapping("/", "/dex/{id}", "/users/{id}", "/users/{userId}/dex/{dexId}")
    fun home(): ModelAndView = ModelAndView("index")
}
