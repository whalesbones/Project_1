package generator;

import java.time.Instant;
import pojo.User;
import com.github.javafaker.Faker;

public class UserGenerator {

    private static final String DOMAIN = "@yandex.ru";

    static Faker faker = new Faker();

    // Генерация email с временной меткой
    private static String generateEmail() {
        return faker.internet().safeEmailAddress();

    }

    // Генерация имени
    private static String generateName() {
        return faker.name().firstName();
    }

    // Генерация пароля (можно использовать faker или простой шаблон)
    private static String generatePassword() {
        return faker.internet().password();
    }


    public static User generate() {
        return new User(
                generateEmail(),
                generateName(),
                generatePassword()
        );
    }


    public static User generate(String customName) {
        return new User(
                generateEmail(),
                customName,
                generatePassword()
        );
    }
}