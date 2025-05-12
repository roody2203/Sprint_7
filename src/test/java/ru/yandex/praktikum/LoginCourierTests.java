package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    private ValidatableResponse response;

    private boolean delete = true;

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
        createCourier = new CreateCourier();
        loginCourier = new LoginCourier();
        deleteCourier = new DeleteCourier();
    }

    @Step("Generate random login")
    public String generateRandomLogin() {
        return RandomStringUtils.randomAlphabetic(7);
    }

    @Step("Generate random password")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Send POST request to /api/v1/courier without first name")
    public ValidatableResponse sendPostRequestForCreateCourierWithoutFirstName(String login, String password) {
        response = createCourier.createCourierWithoutFirstName(login, password);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public ValidatableResponse loginCourierInSystem(String login, String password) {
        response = loginCourier.loginCourier(login, password);
        return response;
    }

    @Step("Compare response status code with extends status code")
    public  void compareStatusCode(ValidatableResponse response, int statusCode){
        response.statusCode(statusCode);
    }

    @Step("Compare response body key is not null")
    public  void compareBodyIsNotNull(ValidatableResponse response, String key) {
        response.body(key, notNullValue());
    }

    @Step("Compare response body message")
    public  void compareBodyMessage(ValidatableResponse response, String result, String extendsBody) {
        response.body(result, is(extendsBody));
    }

    @Test
    @DisplayName("Check response status code login courier")//имя теста
    public void checkLoginCourierShouldReturnStatusCode200Test() {//проверка статус кода ответа при получении логина курьера в системе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
                compareStatusCode(response, 200);//проверяем статус код
    }

    @Test
    @DisplayName("Check response body contains id login courier")//имя теста
    public void checkLoginCourierShouldReturnIdTest() {//проверка тела ответа при получении логина курьера в системе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
                compareBodyIsNotNull(response, "id");//проверяем тело ответа
    }

    @Test
    @DisplayName("Check response status code login courier without login")//имя теста
    public void checkLoginCourierWithoutLoginShouldReturnStatusCode400Test() {//проверка статус кода ответа при передаче запроса без логина
        //создаем пустой логин и рандомный пароль
        login = "";
        password = generateRandomPassword();
        delete = false;
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
                compareStatusCode(response, 400);//проверяем статус код
    }

    @Test
    @DisplayName("Check response body login courier without login")//имя теста
    public void checkLoginCourierWithoutLoginShouldReturnMessageTest() {//проверка тела ответа при передаче запроса без логина
        //создаем пустой логин и рандомный пароль
        login = "";
        password = generateRandomPassword();
        String extendsMessage = "Недостаточно данных для входа";
        delete = false;
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
                compareBodyMessage(response, "message", extendsMessage);//проверяем статус код
    }

    @Test
    @DisplayName("Check response status code login courier without password")//имя теста
    public void checkLoginCourierWithoutPasswordShouldReturnStatusCode400Test() {//проверка статус кода ответа при передаче запроса без пароля
        //создаем рандомный логин и пустой пароль
        login = generateRandomLogin();
        password = "";
        delete = false;
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
        compareStatusCode(response, 400);//проверяем статус код
    }

    @Test
    @DisplayName("Check response body login courier without password")
    public void checkLoginCourierWithoutPasswordShouldReturnMessageTest() {//проверка тела ответа при передаче запроса без пароля
        //создаем рандомный логин и пустой пароль
        login = generateRandomLogin();
        password = "";
        String extendsMessage = "Недостаточно данных для входа";

        delete = false;
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //логинимся с данными созданного курьера
        response = loginCourierInSystem(login, password);
        compareBodyMessage(response, "message", extendsMessage);//проверяем статус код
    }

    @Test
    @DisplayName("Check response status code login courier with fake login")
    public void checkLoginCourierWithFakeLoginShouldReturnStatusCode404Test() {//проверка статус кода ответа при неверной передаче логина в запросе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //передаем неверный логин
        response = loginCourierInSystem("Pisuke", password);
        compareStatusCode(response, 404);//проверяем статус код ответа
    }


    @Test
    @DisplayName("Check response body login courier with fake login")
    public void checkLoginCourierWithFakeLoginShouldReturnMessageTest() {//проверка тела ответа при неверной передаче логина в запросе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        String extendsMessage = "Учетная запись не найдена";
        //создаем курьера с данными выше
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //передаем неверный логин
        response = loginCourierInSystem("Pisuke", password);
        compareBodyMessage(response, "message", extendsMessage);//проверяем статус код ответа
    }

    @Test
    @DisplayName("Check response status code login courier with fake password")
    public void checkLoginCourierWithFakePasswordShouldReturnStatusCode404Test() {//проверка статус кода ответа при неверной передаче пароля в запросе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //создаем курьера
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //передаем неверный пароль
        response = loginCourierInSystem(login, "password");
        compareStatusCode(response, 404);//проверяем статус код ответа
    }


    @Test
    @DisplayName("Check response body login courier with fake password")
    public void checkLoginCourierWithFakePasswordShouldReturnMessageTest() {//проверка тела ответа при неверной передаче пароля в запросе
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        String extendsMessage = "Учетная запись не найдена";
        //создаем курьера с данными выше
        sendPostRequestForCreateCourierWithoutFirstName(login, password);
        //передаем неверный пароль
        response = loginCourierInSystem(login, "password");
        compareBodyMessage(response, "message", extendsMessage);//проверяем статус код ответа
    }

    @Test
    @DisplayName("Check response status code login courier with fake login and password")
    public void checkLoginCourierWithFakeLoginAndPasswordShouldReturnStatusCode404Test() {//проверка с несуществующим пользователем
        delete = false;

        response = loginCourierInSystem("login", "password");
        compareStatusCode(response, 404);//проверяем статус код ответа
    }

    @Test
    @DisplayName("Check response body login courier with fake login and password")
    public void checkLoginCourierWithFakeLoginAndPasswordShouldReturnMessageTest() {//проверка с несуществующим пользователем
        delete = false;

        String extendsMessage = "Учетная запись не найдена";

        response = loginCourierInSystem("login", "password");
        compareBodyMessage(response, "message", extendsMessage);//проверяем статус код ответа
    }

    @After
    public void teardown() {
        if(delete) {
            //удаляем созданного курьера
            Integer id = loginCourier.loginCourier(login, password).extract().path("id");
            deleteCourier.deleteCourier(id);
        }
    }
}
