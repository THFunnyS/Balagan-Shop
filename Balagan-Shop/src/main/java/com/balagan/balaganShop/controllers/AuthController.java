package com.balagan.balaganShop.controllers;


import com.balagan.balaganShop.service.ManagerService;
import com.balagan.balaganShop.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")

public class AuthController {
    private final JwtUtil jwtUtil;
    private final ManagerService managerService;

    public AuthController(JwtUtil jwtUtil, ManagerService managerService) {
        this.jwtUtil = jwtUtil;
        this.managerService = managerService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data, HttpServletResponse response) {
        try {
            String login = data.get("login");
            String password = data.get("password");

            // 1. Аутентификация и получение токена
            String jwt = managerService.authenticate(login, password);

            // 2. Создание куки
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); // 1 час
            // jwtCookie.setSecure(true); // включить в проде (для HTTPS)

            // 3. Добавление куки в ответ
            response.addCookie(jwtCookie);

            // 4. Ответ
            return ResponseEntity.ok("Успешный вход");

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
