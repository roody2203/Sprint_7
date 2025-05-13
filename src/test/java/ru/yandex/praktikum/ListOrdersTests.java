package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.steps.ListOrders;

import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersTests {
    ListOrders listOrders = new ListOrders();

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    @Step("Compare response status code with expected")
    public  void compareStatusCode(ValidatableResponse response, int statusCode) {
        response.statusCode(statusCode);
    }

    @Step("Check list orders is not null")
    public void checkBodyIsNotNull(ValidatableResponse response, String path) {
        response.body(path, notNullValue());
    }

    @Test
    @DisplayName("Check list orders")
    public void listOrdersTest() {
        ValidatableResponse response = listOrders.getListOrders();
        compareStatusCode(response, HttpStatus.SC_OK);
        checkBodyIsNotNull(response, "orders");
    }
}
