package user;

import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.UserApiRequests;
import serialization.User;
import url.BaseUrl;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegistrationUserTest {

    User user = new User("ivanglushenkov@yandex.ru", "Ivan", "qwerty12345");
    UserApiRequests userApiRequests = new UserApiRequests();
    private String accessToken;

    @Before
    public void setUp(){
        BaseUrl.setUp();
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void testCreateUser() {
        Response response = userApiRequests.createUser(user);
        response.then().statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация пользователя с данными уже зарегистрированного пользователя")
    public void testCreateDuplicateUser() {
        userApiRequests.createUser(user);
        Response response = userApiRequests.createUser(user);
        response.then().statusCode(403)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    public void testCreateUserWithoutEmail() {
        user.setEmail("");
        Response response = userApiRequests.createUser(user);
        response.then().statusCode(403)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void testCreateUserWithoutPassword() {
        user.setPassword("");
        Response response = userApiRequests.createUser(user);
        response.then().statusCode(403)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void testCreateUserWithoutName() {
        user.setName("");
        Response response = userApiRequests.createUser(user);
        response.then().statusCode(403)
                .assertThat()
                .body("success", equalTo(false));
    }

    @After
    public void deleteUser(){
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
        if (accessToken!=null) {
            userApiRequests.deleteUser(accessToken);
        }
    }

}
