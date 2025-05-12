package ru.yandex.praktikum.dto;

import lombok.Data;

@Data
public class LoginCourierRequest {
    private String login;

    private String password;
}
