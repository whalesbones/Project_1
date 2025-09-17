package api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreateUserResponse;
import pojo.User;
import steps.UserSteps;
import static generator.UserGenerator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        // Подготовка: генерируем пользователя
        user = generate();
    }

    @Test
    public void createUser_Success() {
        // Действие: регистрируем пользователя
        ValidatableResponse response = userSteps.createUser(user);
        CreateUserResponse actualResponse = response.extract().as(CreateUserResponse.class);

        // Проверки
        assertThat(actualResponse.isSuccess(), equalTo(true));
        assertThat(actualResponse.getUser().getEmail(), equalTo(user.getEmail()));
        assertThat(actualResponse.getUser().getName(), equalTo(user.getName()));
        assertThat(actualResponse.getAccessToken(), notNullValue());
        assertThat(actualResponse.getRefreshToken(), notNullValue());

        accessToken = actualResponse.getAccessToken();
    }

    @Test
    public void createUser_EmailExists_Failure() {
        // Подготовка: создаём пользователя
        userSteps.createUser(user).assertThat().statusCode(200);

        // Действие: пытаемся создать снова
        ValidatableResponse response = userSteps.createUser(user);
        response.assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", notNullValue());
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}