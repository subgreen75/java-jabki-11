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
                Library.addBook();
                break;
            case 2:
                System.out.println("Вы выбрали Пункт 2. Добавить читателя");
                Library.addUser();
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
                Library.findBook();
                break;
            case 6:
                System.out.println("Вы выбрали Пункт 6. Поиск пользователя по ID");
                Library.findUser();
                break;
            case 7:
                System.out.println("Вы выбрали Пункт 7. Выдача книги");
                Library.lendingBook();
                break;
            case 8:
                System.out.println("Вы выбрали Пункт 8. Возврат книги");
                Library.returnBook();
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
}

