package com.balagan.balaganShop.dto;

import lombok.Data;
import lombok.Getter;

/**
 * DTO (Data Transfer Object) для запроса авторизации.
 * Используется для передачи логина и пароля от клиента к серверу.
 */
@Getter // Lombok-аннотация для генерации геттеров
@Data   // Lombok-аннотация для генерации геттеров, сеттеров, toString(), equals(), hashCode()
public class LoginRequest {

    // Поле для хранения логина пользователя
    private String login;

    // Поле для хранения пароля пользователя
    private String password;

    // Явно определённый сеттер для логина (можно не указывать, если использовать @Setter от Lombok)
    public void setLogin(String login) {
        this.login = login;
    }

    // Явно определённый сеттер для пароля
    public void setPassword(String password) {
        this.password = password;
    }
}
