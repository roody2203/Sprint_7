package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
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

    @Step("Check list orders is not null value")
    public void checkBodyListOrdersIsNotNull(ValidatableResponse response, String path) {
        response.body(path, notNullValue());
    }

    @Test
    @DisplayName("Check response body contains list orders create order")
    public void checkStatusCodeListOrdersShouldReturn200Test() {
        ValidatableResponse response = listOrders.getListOrders();
        checkBodyListOrdersIsNotNull(response, "orders");
    }
}
