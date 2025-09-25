package service;

import exception.BookNotAvailableCopies;
import exception.BookNotFoundByID;
import exception.LendingNotFoundByBookID;
import exception.LendingNotFoundByUserID;
import exception.UserNotFoundByID;
import model.Book;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LibraryUtils {
    // метод добавляет книгу в мап books
    // saveToFileFlag - записывать или нет в файл новую книгу. по умолчанию (при загрузке из файле не перезаписываем)
    public static void addBook(String title, String author, int year, int totalCopies, boolean saveToFileFlag ) {
        Book book = new Book(title, author, year, totalCopies);
        Boolean existsBookFlag = false;
        HashMap<Integer, Book> findBooks;
        findBooks = LibraryUtils.getBooks(0, title, author, year);
        if (findBooks.size() == 0) {
            // если не нашли - то добавляем
            Library.books.put(book.getId(), book);
            if (saveToFileFlag) {
                saveBookToFile(book);
            }
        }
        //если нашли только одну книгу
        if (findBooks.size() == 1) {
            for (Book bookFind : findBooks.values()) {
                //увеличиваем общее количество и доступное
                bookFind.setTotalCopies(bookFind.getTotalCopies() + totalCopies);
                bookFind.setAvailableCopies(bookFind.getAvailableCopies() + totalCopies);
                //обновляем мап
                Library.books.put(bookFind.getId(), bookFind);
            }
        }
        //если нашли более одной книги - сообщаем. ничего не делаем
        if (findBooks.size() > 1) {
            System.out.printf("В картотеке найдено бюолее одной книги %s, автора %s и годом издания %d. Книга не добавлена. Привидите порядок картотеку",title, author, year);
        }

    }

    // метод добавляет книгу в мап users
    // saveToFileFlag - записывать или нет в файл новую книгу. по умолчанию (при загрузке из файле не перезаписываем)
    public static void addUser(String name, String email, boolean saveToFileFlag) {
        User user = new User(name, email);
        Library.users.put(user.getId(), user);
        if (saveToFileFlag) {
            saveUserToFile(user);
        }
    }

    // выводит в консоль списки книг в параметре мапе books
    public static void displayBooks(Map<Integer, Book> books) {
        System.out.printf("Найдено книг: %d\n", books.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            entry.getValue().displayBook();
        }
    }

    // выводит в консоль списки читателей в параметре мапе users
    public static void displayUsers(Map<Integer, User> users) {
        System.out.printf("Найдено читателей : %d\n", users.size());
        System.out.println("Список:");
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().displayUser();
        }
    }

    // метод - ищет по id, названию,автору, году книги и вовзращает мап типа books
    public static HashMap<Integer, Book> getBooks(int id, String title, String author, int year) {
        HashMap<Integer, Book> booksRes = new HashMap<>();
        if (id != 0 && Library.books.containsKey(id)) {
            booksRes.put(id, Library.books.get(id));
        } else {
            for (Book book : Library.books.values()) {
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
        if (id != 0 & Library.users.containsKey(id)) {
            usersRes.put(id, Library.users.get(id));
        } else {
            for (User user : Library.users.values()) {
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
        System.out.printf("Количество всего: %d\n", Library.lendingBooks.size());
        for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : Library.lendingBooks.entrySet()) {
            for (Map.Entry<Integer, Integer> bookEntry : entry.getValue().entrySet()) {
                System.out.printf("Читатель: %s(%d); Книга: %s(%d); Количество книг на руках: %d\n", Library.users.get(entry.getKey()).getName(), entry.getKey(), Library.books.get(bookEntry.getKey()).getTitle(), bookEntry.getKey(), bookEntry.getValue());
            }
        }
    }

    //метод выдачи книги для читателя с userID и книги с  bookID. lendingCopies - количетсво выдаваемых книг
    public static void lendingBook(int userID, int bookID, int lendingCopies) throws UserNotFoundByID, BookNotAvailableCopies {
        Book book = Library.books.get(bookID);
        //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
        if (!Library.users.containsKey(userID)) {
            throw new UserNotFoundByID(userID);
        }
        //если пытаются выдать больше чем есть в наличии - выходим
        if (book.getAvailableCopies() < lendingCopies) {
            throw new BookNotAvailableCopies(book.getTitle(), book.getAvailableCopies());
        }
        if (lendingCopies <= 0) {
            throw new IllegalArgumentException("Количество выдачи должно быть больше 0");
        }
        //уменьшаем кол-во доступных
        book.setAvailableCopies(book.getAvailableCopies() - lendingCopies);
        //добавлем в мап lendingBooks информацию в выданной книге
        HashMap<Integer, Integer> lendingBookOnUser;
        if (Library.lendingBooks.containsKey(userID)) {
            lendingBookOnUser = Library.lendingBooks.get(userID);
        } else {
            lendingBookOnUser = new HashMap<>();
        }
        if (lendingBookOnUser.containsKey(bookID)) {
            lendingBookOnUser.put(bookID, lendingBookOnUser.get(bookID) + 1);
        } else {
            lendingBookOnUser.put(bookID, 1);
        }
        Library.lendingBooks.put(userID, lendingBookOnUser);
        System.out.printf("Книга %s, выдано %d\n", book.getTitle(), lendingCopies);

    }

    //метод возврата книги для читателя с userID и книги с  bookID.
    public static void returnBook(int userID, int bookID) {
        try {
            //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
            if (!Library.users.containsKey(userID)) {
                throw new UserNotFoundByID(userID);
            }
            if (!Library.books.containsKey(bookID)) {
                throw new BookNotFoundByID(bookID);
            }
            //проврим, а есть ли в мапе выданных книг та, которую пытаются вернуть. если нет, выходим
            HashMap<Integer, Integer> lendingBookOnUser;
            if (!Library.lendingBooks.containsKey(userID)) {
                throw new LendingNotFoundByUserID(userID);
            }
            lendingBookOnUser = Library.lendingBooks.get(userID);
            if (!lendingBookOnUser.containsKey(bookID)) {
                throw new LendingNotFoundByBookID(userID, bookID);
            }
            //увеливаем доступное кол-во книг в мапе books
            Library.books.get(bookID).setAvailableCopies(Library.books.get(bookID).getAvailableCopies() + lendingBookOnUser.get(bookID));
            //вносим информацию в возвращаенных книгах в мап lendingBooks
            lendingBookOnUser.remove(bookID);
            if (lendingBookOnUser.size() == 0) {
                Library.lendingBooks.remove(userID);
            }
            else {
                Library.lendingBooks.put(userID, lendingBookOnUser);
            }
            System.out.printf("Книга %s, вернули\n", Library.books.get(bookID).getTitle());
        } catch (UserNotFoundByID e) {
            System.out.println(e.getMessage());
        }
        catch (BookNotFoundByID e) {
            System.out.println(e.getMessage());
        }
        catch (LendingNotFoundByUserID e) {
            System.out.println(e.getMessage());
        }
        catch (LendingNotFoundByBookID e) {
            System.out.println(e.getMessage());
        }
    }

    //записывает книгу в файл
    public static void saveBookToFile(Book book) {
        try (FileWriter writer = new FileWriter(Library.csvFileBook, true)) {
            writer.write("\n" + book.getTitle() + ";" + book.getAuthor() + ";" + book.getYear() + ";" + book.getAvailableCopies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //записывает читателя в файл
    public static void saveUserToFile(User user) {
        try (FileWriter writer = new FileWriter(Library.csvFileUser, true)) {
            writer.write("\n" + user.getName() + ";" + user.getEmail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
