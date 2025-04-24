package com.balagan.balaganShop.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequest {
    private String login;
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
