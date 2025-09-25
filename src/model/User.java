package model;

import java.util.regex.Pattern;

public class User {
    private final int id;
    private String name;
    private String email;
    private static int startId = 1;

    public User(String name, String email) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Некорректное значение атрибутов");
        }
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Некорректное значение email");
        }
        this.id = this.nextId();
        this.name = name;
        this.email = email;
    }

    private int nextId() {
        return startId++;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public int getId() {
        return this.id;
    }

    public void displayUser() {
        System.out.printf("ID: %d, Читатель: %s, Адрес: %s\n", this.getId(), this.getName(),  this.getEmail());
    }

    private boolean validateEmail(String email) {
      return Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
              .matcher(email)
              .matches();
    }


}