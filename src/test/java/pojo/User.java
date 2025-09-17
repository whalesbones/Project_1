// src/test/java/pojo/User.java
package pojo;

import java.util.Objects;

public class User {
    private String email;
    private String password;
    private String name;

    // ✅ Конструктор по умолчанию
    public User() {}

    // Конструктор с параметрами
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // ✅ Геттеры
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }

    // ✅ Сеттеры
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }

    // equals() и hashCode() для сравнения
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name);
    }
}