package com.balagan.balaganShop.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) для ответа при успешной аутентификации.
 * Содержит только один параметр — JWT токен, который клиент будет использовать для дальнейших запросов.
 */
@Data // Lombok-аннотация, автоматически генерирует геттеры, сеттеры, toString(), equals() и hashCode()
public class JwtResponse {

    // Поле для хранения сгенерированного JWT токена
    private String token;

    // Конструктор, который инициализирует объект с переданным токеном
    public JwtResponse(String token){
        this.token = token;
    }
}
