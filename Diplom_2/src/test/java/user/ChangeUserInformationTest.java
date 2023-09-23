package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.UserApiRequests;
import serialization.User;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserInformationTest {

    User user = new User("ivanglushenkov@yandex.ru", "Ivan", "qwerty12345");
    UserApiRequests userApiRequests = new UserApiRequests();
    private String accessToken;
    private String updateEmail = "MatrixNeo@yandex.ru";
    private String updateName = "Neo";

    @Before
    public void setUp(){
        url.BaseUrl.setUp();
        userApiRequests.createUser(user);
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение email у авторизованного пользователя")
    public void testUpdateEmailAuthUser(){
        user.setEmail(updateEmail);
        userApiRequests.changeDataUser(user,accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени у авторизованного пользователя")
    public void checkUpdateNameAuthUser(){
        user.setName(updateName);
        userApiRequests.changeDataUser(user,accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение email у неавторизованного пользователя")
    public void checkUpdateEmailNotAuthUser(){
        accessToken="";
        user.setEmail(updateEmail);
        userApiRequests.changeDataUser(user,accessToken)
                .then().assertThat()
                .body("success", equalTo(false)).
                and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение имени у неавторизованного пользователя")
    public void checkUpdateNameNotAuthUser() {
        accessToken="";
        user.setName(updateName);
        userApiRequests.changeDataUser(user, accessToken)
                .then().assertThat()
                .body("success", equalTo(false)).
                and()
                .statusCode(401);
    }

    @After
    public void deleteUser(){
        if (accessToken!=null) {
            userApiRequests.deleteUser(accessToken);
        }
    }
}
