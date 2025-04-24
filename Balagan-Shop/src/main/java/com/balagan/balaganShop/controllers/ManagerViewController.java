package com.balagan.balaganShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerViewController {

    @GetMapping("/manager/login")
    public String showLoginPage(Model model) {
        model.addAttribute("title", "Вход менеджера");
        return "manager-login";
    }
}

