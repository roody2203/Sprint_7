package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class DeleteCourier {
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .pathParam("id", id)
                .when()
                .delete("/api/v1/courier/{id}")
                .then();
    }
}
