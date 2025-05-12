package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.steps.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;



@RunWith(Parameterized.class)
public class CreateOrderTests {

    private String[] colors;
    private Integer track;//создаем поле цвет


    private CreateOrder createOrder;
    private CancelOrder cancelOrder;

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        createOrder = new CreateOrder();
        cancelOrder = new CancelOrder();
    }

    @Parameterized.Parameters(name = "Тестовые данные: {index}")
    public static Iterable<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                { new String[] {"BLACK"}},
                { new String[] {"GREY"}},
                { new String[] {"BLACK", "GREY"}},
                { new String[] {}}
        });
    }

    public CreateOrderTests(String[] colors) {
        this.colors = colors;
    }

    @Step("Send POST request to /api/v1/orders")
    public ValidatableResponse createOrder(String[] colors) {
           ValidatableResponse response =  createOrder.createOrder(colors);
           return response;
    }


    @Step("Compare response status code with extends status code")
    public  void compareStatusCode(ValidatableResponse response, int statusCode) {
        response.statusCode(statusCode);
    }

    @Step("Get track")
        public void getTrack(ValidatableResponse response, String path) {
            track = response.extract().path(path);
    }

    @Step("Compare response status code with extends status code")
    public  void compareTrackIsNotValue(ValidatableResponse response, String path) {
        response.body(path, notNullValue());
    }


    @Test
    @DisplayName("Check response status code create order")
    public void checkCreateOrderShouldReturnStatusCode201Test() {//проверка статус кода ответа при создании заказа в системе
        //создаем заказ
        ValidatableResponse response = createOrder.createOrder(colors);
        compareStatusCode(response, 201);
        getTrack(response, "track");
    }

    @Test
    @DisplayName("Check response body contains 'track' create order")
    public void checkCreateOrderShouldReturnTrackTest() {//проверка тела ответа при создании заказа в системе
        //создаем заказ
        ValidatableResponse response = createOrder.createOrder(colors);
        compareTrackIsNotValue(response, "track");
        getTrack(response, "track");
    }

    @After
    public void teardown() {
        if(track != null) {
            //удаляем созданного курьера
            cancelOrder.cancelOrder(track);
        }
    }
}