package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        boolean isManager = false;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && JwtUtil.validateToken(token)) {
                        isManager = true;
                        break;
                    }
                }
            }
        }

        model.addAttribute("isManager", isManager);
        model.addAttribute("title", "Главная страница");
        return "home";
    }

}