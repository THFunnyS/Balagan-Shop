package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletResponse;
import com.balagan.balaganShop.service.ManagerService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ManagerViewController {
    private final JwtUtil jwtUtil;
    private final ManagerService managerService;

    public ManagerViewController(JwtUtil jwtUtil, ManagerService managerService) {
        this.jwtUtil = jwtUtil;
        this.managerService = managerService;
    }

    @GetMapping("/manager/login")
    public String showLoginPage(Model model) {
        model.addAttribute("title", "Вход менеджера");
        return "manager-login";
    }

    @PostMapping("manager/login")
    public String managerLogin(@RequestParam String login,
                               @RequestParam String password, HttpServletResponse response, Model model){
        try {
            // 1. Аутентификация и получение токена
            String jwt = managerService.authenticate(login, password);
            // 2. Создание куки
            Cookie jwtCookie = new Cookie("token", jwt);
            jwtCookie.setHttpOnly(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); // 1 час
            response.addCookie(jwtCookie);

            return "redirect:/";
        } catch (RuntimeException e){
            model.addAttribute("error", e.getMessage());
            return "manager-login";
        }
    }

    @PostMapping("manager/logout")
    public String managerLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            jwtUtil.blacklistToken(token);
        }

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        return "redirect:/";
    }
}