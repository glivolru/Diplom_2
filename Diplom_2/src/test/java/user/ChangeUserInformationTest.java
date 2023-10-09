package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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
    public void setUp() {
        url.BaseUrl.setUp();
        userApiRequests.createUser(user);
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение email у авторизованного пользователя")
    public void testUpdateEmailAuthUser() {
        user.setEmail(updateEmail);
        Response response = userApiRequests.changeDataUser(user, accessToken);
        response.then().statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени у авторизованного пользователя")
    public void checkUpdateNameAuthUser() {
        user.setName(updateName);
        Response response = userApiRequests.changeDataUser(user, accessToken);
        response.then().statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение email у неавторизованного пользователя")
    public void checkUpdateEmailNotAuthUser() {
        accessToken = "";
        user.setEmail(updateEmail);
        Response response = userApiRequests.changeDataUser(user, accessToken);
        response.then().statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Изменение имени у неавторизованного пользователя")
    public void checkUpdateNameNotAuthUser() {
        accessToken = "";
        user.setName(updateName);
        Response response = userApiRequests.changeDataUser(user, accessToken);
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
