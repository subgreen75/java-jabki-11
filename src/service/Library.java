package service;

import model.Book;
import model.User;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Library {
    // мап списки книги. ключ - UserID
    public static HashMap<Integer, Book> books = new HashMap<>();
    // мап списки читателей. ключ - BookID
    public static HashMap<Integer, User> users = new HashMap<>();
    // мап списки выданных книг. ключ - UserID, значения - другой мап (ключ - BookID, значение - количество книг на руках)
    public static HashMap<Integer, HashMap<Integer, Integer>> lendingBooks = new HashMap<>();

    //метод инициализации начальных значений. загружаем из csv файлов src/service/books.csv и src/service/users.csv
    public static void init() {
        loadBooksFromFile();
        loadUsersFromFile();
    }

    // метод добавляет книгу в мап books
    public static void addBook(String title, String author, int year, int totalCopies) {
        Book book = new Book(title, author, year, totalCopies);
        books.put(book.getId(), book);
    }

    // метод добавляет книгу в мап users
    public static void addUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
    }

    // метод загружает из csv файла в мап books
    private static void loadBooksFromFile() {
        books.clear();
        String csvFile = "src/service/books.csv";
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                addBook(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // метод загружает из csv файла в мап users
    private static void loadUsersFromFile() {
        users.clear();
        String csvFile = "src/service/users.csv";
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                addUser(data[0], data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // выводит в консоль списки книг в параметре мапе books
    public static void displayBooks(HashMap<Integer, Book> books) {
        System.out.printf("Найдено книг: %d\n", books.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            entry.getValue().displayBook();
        }
    }

    // выводит в консоль списки читателей в параметре мапе users
    public static void displayUsers(HashMap<Integer, User> users) {
        System.out.printf("Найдено читателей : %d\n", users.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().displayUser();
        }
    }

    // метод - ищет по id, названию,автору, году книги и вовзращает мап типа books
    public static HashMap<Integer, Book> getBooks(int id, String title, String author, int year) {
        HashMap<Integer, Book> booksRes = new HashMap<>();
        if (id != 0 && books.containsKey(id)) {
            booksRes.put(id, books.get(id));
        } else {
            for (Book book : books.values()) {
                if (((title != null && !title.isBlank() && book.getTitle().toUpperCase().matches(".*" + title.toUpperCase() + ".*")) || (title == null || title.isBlank())) &&
                    ((author != null && !author.isBlank() && book.getAuthor().toUpperCase().matches(".*" + author.toUpperCase() + ".*")) || (author == null || author.isBlank())) &&
                     ((year != 0 && book.getYear() == year) || year == 0)
                ) {
                    booksRes.put(book.getId(), book);
                }
            }
        }
        return booksRes;
    }

    // метод - ищет по id, ФИО, емейлу  читателей и вовзращает мап типа users
    public static HashMap<Integer, User> getUsers(int id, String name, String email) {
        HashMap<Integer, User> usersRes = new HashMap<>();
        if (id != 0 & users.containsKey(id)) {
            usersRes.put(id, users.get(id));
        } else {
            for (User user : users.values()) {
                if (((name != null && !name.isBlank() && user.getName().toUpperCase().matches(".*" + name.toUpperCase() + ".*")) || (name == null || name.isBlank())) &&
                        ((email != null && !email.isBlank() && user.getEmail().toUpperCase().matches(".*" + email.toUpperCase() + ".*")) || (email == null || email.isBlank()))
                ) {
                    usersRes.put(user.getId(), user);
                }
            }
        }
        return usersRes;
    }

    //выводим в консоль списки выданных книг
    public static void displayLendingBooks() {
        System.out.println("Список выданных книг:");
        System.out.printf("Количество всего: %d\n", lendingBooks.size());
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : lendingBooks.entrySet()) {
            for (Map.Entry<Integer, Integer> bookEntry : entry.getValue().entrySet()) {
                System.out.printf("Читатель: %s(%d); Книга: %s(%d); Количество книг на руках: %d\n", users.get(entry.getKey()).getName(), entry.getKey(), books.get(bookEntry.getKey()).getTitle(), bookEntry.getKey(), bookEntry.getValue());
            }
        }
    }

    // метод для ввода в консоли
    public static String input(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    // добавляет книгу в мап books
    public static void addBook() {
        String title, author;
        int year, totalCopies;
        try {
            title = input("введите название книги:");
            author = input("введите ФИО автора:");
            year = Integer.parseInt(input("введите год издания:"));
            totalCopies = Integer.parseInt(input("введите количество копий:"));
            Library.addBook(title, author, year, totalCopies);
            System.out.println("Книга добавлена");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // добавляет читателя в мап users
    public static void addUser() {
        String name, email;
        try {
            name = input("введите ФИО читателя:");
            email = input("введите адрес эл.почты читателя:");
            Library.addUser(name, email);
            System.out.println("Читатель добавлен");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим значения названия книги, автора, год издания и ищет книгу по комбинации введенных значений
    public static void findBook() {
        String title, author;
        int year;
        HashMap<Integer, Book> booksFind;
        try {
            title = input("введите название книги :");
            author = input("введите ФИО автора:");
            try {
                year = Integer.parseInt(input("введите год издания:"));
            } catch (Exception e) {
                year = 0;
            }
            booksFind = Library.getBooks(0, title, author, year);
            Library.displayBooks(booksFind);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //вводим значения ID читателя и ищет читателя по введенному значению ID
    public static void findUser() {
        int id;
        HashMap<Integer, User> usersFind;
        try {
            try {
                id = Integer.parseInt(input("введите ID читателя:"));
            } catch (Exception e) {
                id = 0;
            }
            usersFind = Library.getUsers(id, null, null);
            Library.displayUsers(usersFind);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
      вводим название книги, автора, год издания и ищет книгу по комбинации введенных значений
      вводим значение ID читателя
      запускаем метод  book.lendingBook для выдачи книги
     */
    public static void lendingBook() {
        String title, author;
        int year, userID, lendingCopies = 1;
        HashMap<Integer, Book> booksFind;
        try {
            userID = Integer.parseInt(input("введите ID читателя:"));
            title = input("введите название книги :");
            author = input("введите ФИО автора:");
            try {
                year = Integer.parseInt(input("введите год издания:"));
            } catch (Exception e) {
                year = 0;
            }
            booksFind = Library.getBooks(0, title, author, year);
            if (booksFind.size() == 0 || booksFind.size() > 1) {
                System.out.println("По вашему запросу не найдено книг или найдено более одной. Уточните параметры поиска");
                Library.displayBooks(booksFind);
                return;
            }
            for (Book book : booksFind.values()) {
                book.lendingBook(userID, book.getId(), lendingCopies);
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
      вводим значение ID  книги
      вводим значение ID читателя
      запускаем метод  book.returnBook для возврата книги
     */
    public static void returnBook() {
        int bookID, userID;
        userID = Integer.parseInt(input("введите ID читателя:"));
        bookID = Integer.parseInt(input("введите ID книги:"));
        Book book = Library.books.get(bookID);
        book.returnBook(userID, bookID);
    }
}
