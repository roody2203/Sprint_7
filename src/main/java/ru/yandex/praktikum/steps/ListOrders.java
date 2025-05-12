package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ListOrders {

    public ValidatableResponse getListOrders() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .when()
                .get("/api/v1/orders")
                .then();
    }
}
