package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.dto.CancelOrderRequest;
import ru.yandex.praktikum.dto.CreateOrderRequest;
import ru.yandex.praktikum.steps.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderTests {

    private String[] colors;
    private Integer track;//создаем поле цвет


    private CreateOrder createOrder;
    private CancelOrder cancelOrder;


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

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        createOrder = new CreateOrder();
        cancelOrder = new CancelOrder();
    }

    @Step("Compare response status code with expected")
    public  void compareStatusCode(ValidatableResponse response, int statusCode) {
        response.statusCode(statusCode);
    }

    @Step("Get track")
    public Integer getTrack(ValidatableResponse response, String path) {
        return response.extract().path(path);
    }

    @Step("Compare response track is not null")
    public void compareTrackIsNotValue(ValidatableResponse response, String path) {
        response.body(path, notNullValue());
    }

    @Test
    @DisplayName("Check create order")
    public void checkCreateOrderTest() {//проверка при создании заказа в системе
        //создаем заказ
        CreateOrderRequest request = new CreateOrderRequest();
        request.setFirstName("Naruto");
        request.setLastName("Uchiha");
        request.setAddress("Konoha, 142 apt.");
        request.setMetroStation("4");
        request.setPhone("+7 800 355 35 35");
        request.setRentTime("5");
        request.setDeliveryDate("2027-06-06");
        request.setComment("Saske, come back to Konoha");
        request.setColor(colors);
        ValidatableResponse response =  createOrder.createOrder(request);

        compareStatusCode(response, HttpStatus.SC_CREATED);
        compareTrackIsNotValue(response, "track");
        track = getTrack(response, "track");
    }

    @After
    public void teardown() {
        if(track != null) {
            //удаляем созданного курьера
            CancelOrderRequest request = new CancelOrderRequest();
            request.setTrack(track);
            cancelOrder.cancelOrder(request);
        }
    }
}