package user;

import io.qameta.allure.Description;
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
    public void testLoginUser(){
        userApiRequests.loginUser(user)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация с некорректным email")
    public void testLoginUserIncorrectEmail(){
        user.setEmail("uuuuuaaaaa@yandex");
        userApiRequests.loginUser(user)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    public void testLoginUserIncorrectPassword(){
        user.setPassword("@)20$!@$");
        userApiRequests.loginUser(user)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void deleteUser(){
        if (accessToken!=null) {
            userApiRequests.deleteUser(accessToken);
        }
    }
}