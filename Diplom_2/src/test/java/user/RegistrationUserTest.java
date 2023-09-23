package user;

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
    public void testCreateUser(){
        userApiRequests.createUser(user)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Регистрация пользователя с данными уже зареганого пользователя")
    public void testCreateDuplicateUser(){
        userApiRequests.createUser(user);
        userApiRequests.createUser(user)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    public void testCreateUserWithoutEmail(){
        user.setEmail("");
        userApiRequests.createUser(user)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void testCreateUserWithoutPassword(){
        user.setPassword("");
        userApiRequests.createUser(user)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void testCreateUserWithoutName(){
        user.setName("");
        userApiRequests.createUser(user)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @After
    public void deleteUser(){
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
        if (accessToken!=null) {
            userApiRequests.deleteUser(accessToken);
        }
    }

}
