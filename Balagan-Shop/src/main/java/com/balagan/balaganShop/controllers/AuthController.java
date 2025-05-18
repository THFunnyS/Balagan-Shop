package com.balagan.balaganShop.controllers;


import com.balagan.balaganShop.service.ManagerService;
import com.balagan.balaganShop.util.JwtUtil;
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
    public ResponseEntity<String> login(@RequestBody Map<String, String> data){
        try {
            String login = data.get("login");
            String password = data.get("password");
            String jwt = managerService.authenticate(login, password);
            return ResponseEntity.ok(jwt);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            jwtUtil.blacklistToken(token);
            return ResponseEntity.ok("Вы успешно вышли из системы");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка выхода: " + e.getMessage());
        }
    }
}
