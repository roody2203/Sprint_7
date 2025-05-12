package ru.yandex.praktikum.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
}
