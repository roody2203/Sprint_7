package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateCourierRequest;
import ru.yandex.praktikum.dto.LoginCourierRequest;
import ru.yandex.praktikum.steps.CreateCourier;
import ru.yandex.praktikum.steps.DeleteCourier;
import ru.yandex.praktikum.steps.LoginCourier;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTests {

    private CreateCourier createCourier;
    private LoginCourier loginCourier;//создаем объект класса CreateCourier
    private DeleteCourier deleteCourier;

    private String login;//создаем поле логин
    private String password;//создаем поле пароль

    private Integer id;

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        createCourier = new CreateCourier();
        loginCourier = new LoginCourier();
        deleteCourier = new DeleteCourier();

        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();

        //создаем курьера
        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        createCourier.createCourier(request);
    }

    @Step("Generate random login")
    public String generateRandomLogin() {
        return RandomStringUtils.randomAlphabetic(7);
    }

    @Step("Generate random password")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Compare response status code with expected")
    public  void compareStatusCode(ValidatableResponse response, int statusCode){
        response.statusCode(statusCode);
    }

    @Step("Compare response body key is not null")
    public  void compareBodyIsNotNull(ValidatableResponse response, String key) {
        response.body(key, notNullValue());
    }

    @Step("Compare response body message")
    public  void compareBodyMessage(ValidatableResponse response, String result, String expectedBody) {
        response.body(result, is(expectedBody));
    }

    @Test
    @DisplayName("Check login courier")//имя теста
    public void checkLoginCourierTest() {//проверка логина курьера в системе
        //логинимся с данными созданного курьера
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_OK);
        //проверяем тело ответа
        compareBodyIsNotNull(response, "id");
    }

    @Test
    @DisplayName("Check login courier without login")//имя теста
    public void checkLoginCourierWithoutLoginTest() {//проверка при передаче запроса без логина
        //создаем пустой логин и рандомный пароль
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin("");
        request.setPassword(password);

        //логинимся с данными созданного курьера
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        //проверяем тело ответа
        compareBodyMessage(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check login courier without password")//имя теста
    public void checkLoginCourierWithoutPasswordTest() {//проверка при передаче запроса без пароля
        //создаем логин и пустой пароль
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin(login);
        request.setPassword("");

        //логинимся с данными созданного курьера
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        //проверяем тело ответа
        compareBodyMessage(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check login courier with fake login")
    public void checkLoginCourierWithFakeLoginTest() {//проверка при неверной передаче логина в запросе
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin("Pisuke");
        request.setPassword(password);

        //передаем неверный логин
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код ответа
        compareStatusCode(response, HttpStatus.SC_NOT_FOUND);
        //проверяем тело ответа
        compareBodyMessage(response, "message", "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Check login courier with fake password")
    public void checkLoginCourierWithFakePasswordTest() {//проверка при неверной передаче пароля в запросе
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin(login);
        request.setPassword("password");

        //передаем неверный логин
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код ответа
        compareStatusCode(response, HttpStatus.SC_NOT_FOUND);
        //проверяем тело ответа
        compareBodyMessage(response, "message", "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Check login courier with fake login and password")
    public void checkLoginCourierWithFakeLoginAndPasswordTest() {//проверка с несуществующим пользователем
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin("login");
        request.setPassword("password");
        //передаем неверный логин
        ValidatableResponse response = loginCourier.loginCourier(request);
        //проверяем статус код ответа
        compareStatusCode(response, HttpStatus.SC_NOT_FOUND);
        //проверяем тело ответа
        compareBodyMessage(response, "message", "Учетная запись не найдена");
    }

    @After
    public void teardown() {
        //удаляем созданного курьера
        LoginCourierRequest request = new LoginCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        Integer id = loginCourier.loginCourier(request).extract().path("id");
        deleteCourier.deleteCourier(id);
    }
}
