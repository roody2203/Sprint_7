package ru.yandex.praktikum.dto;

import lombok.Data;

@Data //аннотация для создания геттеров, сеттеров, создания конструкторов
public class CreateCourierRequest {
    private String login;

    private String password;

    private String firstName;
}
