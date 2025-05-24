package com.balagan.balaganShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String error403() {
        return "403"; // имя шаблона
    }

    @GetMapping("/401")
    public String error401(){
        return  "401";
    }
}