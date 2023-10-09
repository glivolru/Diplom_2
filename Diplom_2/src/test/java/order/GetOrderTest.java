package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pens.IngredientApiRequests;
import pens.OrderApiRequests;
import pens.UserApiRequests;
import serialization.Ingredient;
import serialization.Order;
import serialization.User;
import url.BaseUrl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {

    url.BaseUrl baseUrlApi = new url.BaseUrl();
    UserApiRequests userApiRequests = new UserApiRequests();
    OrderApiRequests orderApiRequests = new OrderApiRequests();
    IngredientApiRequests ingredientApiRequests = new IngredientApiRequests();
    User user = new User("ivanglushenkov@yandex.ru", "Ivan", "qwerty12345");

    private String accessToken;
    private Ingredient ingredientList;
    private List<String> ingredients;
    private Order order;

    @Before
    public void setUp() {
        BaseUrl.setUp();
        ingredientList = ingredientApiRequests.getIngredientInfo();
        ingredients = new ArrayList<>();
        ingredients.add(ingredientList.getData().get(1).get_id());
        ingredients.add(ingredientList.getData().get(2).get_id());
        ingredients.add(ingredientList.getData().get(3).get_id());
        userApiRequests.createUser(user);
        accessToken = userApiRequests.loginUser(user).then().extract().path("accessToken");
        order = new Order(ingredients);
    }

    @Test
    @DisplayName("Получение списка заказов, авторизованный пользователь")
    public void testGetOrderAuthUser() {
        Response response = orderApiRequests.getOrdersAuthUser(accessToken);
        response.then().statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов, неавторизованный пользователь")
    public void testGetOrderNotAuthUser() {
        Response response = orderApiRequests.getOrdersNotAuthUser();
        response.then().statusCode(401)
                .assertThat()
                .body("success", equalTo(false));
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userApiRequests.deleteUser(accessToken);
        }
    }
}


