package ui;

import java.util.HashMap;
import java.util.Scanner;

import model.Book;
import model.User;
import service.Library;
// Метод Консольное Меню
public class ConsoleMenu {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 0) {
            displayMenu();
            System.out.print("Введите ваш выбор: ");
            choice = scanner.nextInt();
            processChoice(choice, scanner);
        }
        System.out.println("Программа завершена.");
        scanner.close();
    }

    // Метод для отображения пунктов меню
    public static void displayMenu() {
        System.out.println("\n--- Консольное меню ---");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Добавить читателя");
        System.out.println("3. Просмотр всех книг");
        System.out.println("4. Просмотр всех читателей");
        System.out.println("5. Поиск книг по: названию, автору, году");
        System.out.println("6. Поиск пользователя по ID");
        System.out.println("7. Выдача книги");
        System.out.println("8. Возврат книги");
        System.out.println("9. Просмотр всех выданных книг");
        System.out.println("0. Выход");
        System.out.println("-----------------------");
    }

    // Метод для обработки выбора пользователя
    public static void processChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                System.out.println("Вы выбрали Пункт 1. Добавить книгу");
                addBook();
                break;
            case 2:
                System.out.println("Вы выбрали Пункт 2. Добавить читателя");
                addUser();
                break;
            case 3:
                System.out.println("Вы выбрали Пункт 3. Просмотр всех книг");
                Library.displayBooks(Library.books);
                break;
            case 4:
                System.out.println("Вы выбрали Пункт 4. Просмотр всех пользователей");
                Library.displayUsers(Library.users);
                break;
            case 5:
                System.out.println("Вы выбрали Пункт 5. Поиск книг по: названию, автору, году");
                findBook();
                break;
            case 6:
                System.out.println("Вы выбрали Пункт 6. Поиск пользователя по ID");
                findUser();
                break;
            case 7:
                System.out.println("Вы выбрали Пункт 7. Выдача книги");
                lendingBook();
                break;
            case 8:
                System.out.println("Вы выбрали Пункт 8. Возврат книги");
                returnBook();
                break;
            case 9:
                System.out.println("Вы выбрали Пункт 9. Просмотр всех выданных книг");
                Library.displayLendingBooks();
                break;
            case 0:
                break;
            default:
                System.out.println("Неверный ввод. Пожалуйста, выберите один из пунктов меню.");
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