package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ListOrders extends BaseApi {
    public final static String path = "/api/v1/orders";

    @Step("Send GET request to /api/v1/orders")
    public ValidatableResponse getListOrders() {
        return given()
                .spec(requestSpecification)
                .when()
                .get(path)
                .then();
    }
}
