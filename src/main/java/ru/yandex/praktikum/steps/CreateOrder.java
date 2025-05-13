package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class CreateOrder extends BaseApi {
    public final static String path = "/api/v1/orders";

    @Step("Send POST request to /api/v1/orders")
    public ValidatableResponse createOrder(CreateOrderRequest request) {
        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .post(path)
                .then();
    }
}
