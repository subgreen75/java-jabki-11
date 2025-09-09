package model;

public class User {
    private final int id;
    private String name;
    private String email;
    private static int startId = 1;

    public User(String name, String email) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Некорректное значение атрибутов");
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
        System.out.printf("Читатель: %s (%d), Адрес: %s\n", this.getName(), this.getId(), this.getEmail());
    }
}