package service;

import exception.BookNotAvailableCopies;
import exception.BookNotFoundByID;
import exception.LendingNotFoundByBookID;
import exception.LendingNotFoundByUserID;
import exception.UserNotFoundByID;
import model.Book;
import model.User;

import java.io.FileWriter;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Library {
    // мап списки книги. ключ - UserID
    public static HashMap<Integer, Book> books = new HashMap<>();
    // мап списки читателей. ключ - BookID
    public static HashMap<Integer, User> users = new HashMap<>();
    // мап списки выданных книг. ключ - UserID, значения - другой мап (ключ - BookID, значение - количество книг на руках)
    public static HashMap<Integer, HashMap<Integer, Integer>> lendingBooks = new HashMap<>();
    //файл scv книг с исходными данными
    public static String csvFileBook = "src/resources/books.csv";
    //файл scv книг с исходными данными
    public static String csvFileUser = "src/resources/users.csv";

    //метод инициализации начальных значений. загружаем из csv файлов src/service/books.csv и src/service/users.csv
    public static void init() {
        loadBooksFromFile();
        loadUsersFromFile();
    }



    // метод загружает из csv файла в мап books
    private static void loadBooksFromFile() {
        books.clear();
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileBook))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                LibraryUtils.addBook(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // метод загружает из csv файла в мап users
    private static void loadUsersFromFile() {
        users.clear();
        String line;
        String csvSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileUser))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                LibraryUtils.addUser(data[0], data[1], false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}