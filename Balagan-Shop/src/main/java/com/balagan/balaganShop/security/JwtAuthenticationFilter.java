package com.balagan.balaganShop.security;

import com.balagan.balaganShop.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        // 1. Получаем заголовок Authorization
        final String authHeader = request.getHeader("Authorization");
        // 2. Проверяем наличие и формат заголовка
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // 3. Извлекаем токен (после "Bearer ")
            //filterChain.doFilter(request, response);
            //return;
        } else {
            // Иначе пытаемся достать токен из cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }
        // 4. Проверяем токен
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isTokenBlacklisted(jwt)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Токен недействителен");
            return;
        }
        try {
            String login = jwtUtil.extractLogin(jwt);
            String role = jwtUtil.extractRole(jwt);

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(jwt)) {
                    // 5. Создаём объект аутентификации
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    login,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase()))
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    // 6. Сохраняем в SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT просрочен");
            return;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Недопустимый токен");
            return;
        }
        // 7. Передаём запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}