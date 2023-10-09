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

public class LoginUserTest {
    User user = new User("ivanglushenkov@yandex.ru", "Ivan", "qwerty12345");
    UserApiRequests userApiRequests = new UserApiRequests();
    private String accessToken;
    @Before
    public void setUp(){
        BaseUrl.setUp();
        userApiRequests.createUser(user);
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void testLoginUser() {
        Response response = userApiRequests.loginUser(user);
        response.then().statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с некорректным email")
    public void testLoginUserIncorrectEmail() {
        user.setEmail("uuuuuaaaaa@yandex");
        Response response = userApiRequests.loginUser(user);
        response.then().statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void testLoginUserIncorrectPassword() {
        user.setPassword("@)20$!@$");
        Response response = userApiRequests.loginUser(user);
        response.then().statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
    }


    @After
    public void deleteUser(){
        if (accessToken!=null) {
            userApiRequests.deleteUser(accessToken);
        }
    }
}