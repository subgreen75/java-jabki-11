package model;

import exception.bookNotAvailableCopies;
import exception.bookNotFoundByID;
import exception.bookReturnGreaterThanAllowed;
import exception.lendingNotFoundByBookID;
import exception.lendingNotFoundByUserID;
import exception.userNotFoundByID;
import service.Library;

import java.util.HashMap;

public class Book {
    private final int id;
    private String title;
    private String author;
    private int year;
    private int totalCopies;
    private int availableCopies;
    //стартовое значение для bookID
    private static int startId = 1;


    public Book(String title, String author, int year, int totalCopies) {
        if (title == null || author == null || title.isBlank() || author.isBlank() || year < 0 || totalCopies < 0) {
            throw new IllegalArgumentException("Некорректное значение атрибутов книги");
        }
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.id = this.nextId();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getYear() {
        return this.year;
    }

    public int getTotalCopies() {
        return this.totalCopies;
    }


    private int nextId() {
        return startId++;
    }

    public int getAvailableCopies() {
        return this.availableCopies;
    }

    //выводит в консоль информацию о книге
    public void displayBook() {
        System.out.printf("Название: %s(%d), Автор: %s, Год издания: %d, Всего копий: %d, В наличии %d\n", this.getTitle(), this.getId(), this.getAuthor(), this.getYear(), this.getTotalCopies(), this.getAvailableCopies());
    }

    //метод выдачи книги для читателя с userID и книги с  bookID. lendingCopies - количетсво выдаваемых книг
    public static void lendingBook(int userID, int bookID, int lendingCopies)  {
        Book book = Library.books.get(bookID);
        try {
             //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
             if (!Library.users.containsKey(userID)) {
                 throw new userNotFoundByID(userID);
             }
             //если пытаются выдать больше чем есть в наличии - выходим
             if (book.availableCopies < lendingCopies) {
                 throw new bookNotAvailableCopies(book.getTitle(), book.getAvailableCopies());
             }
             if (lendingCopies <=0) {
                 throw new IllegalArgumentException("Количество выдачи должно быть больше 0");
             }
            //уменьшаем кол-во доступных
            book.availableCopies = book.availableCopies - lendingCopies;
            //добавлем в мап lendingBooks информацию в выданной книге
            HashMap<Integer, Integer> lendingBookOnUser;
            if (Library.lendingBooks.containsKey(userID)) {
                lendingBookOnUser = Library.lendingBooks.get(userID);
            }
            else {
                lendingBookOnUser = new HashMap<>();
            }
            if (lendingBookOnUser.containsKey(bookID)) {
                lendingBookOnUser.put(bookID, lendingBookOnUser.get(bookID) + 1);
            }
            else {
                lendingBookOnUser.put(bookID, 1);
            }
            Library.lendingBooks.put(userID,lendingBookOnUser);
            System.out.printf("Книга %s, выдано %d\n", book.getTitle(), lendingCopies);
            } catch (bookNotAvailableCopies e) {
                System.out.println(e.getMessage());
            }
            catch (userNotFoundByID e) {
                System.out.println(e.getMessage());
            }

    }

    //метод возврата книги для читателя с userID и книги с  bookID.
    public static void returnBook(int userID, int bookID) {
        try {
            //проверим есть ли  читатели и книги с такими  userID  и bookID. если нет - выходим
            if (!Library.users.containsKey(userID)) {
                throw new userNotFoundByID(userID);
            }
            if (!Library.books.containsKey(bookID)) {
                throw new bookNotFoundByID(bookID);
            }
            //проврим, а есть ли в мапе выданных книг та, которую пытаются вернуть. если нет, выходим
            HashMap<Integer, Integer> lendingBookOnUser;
            if (!Library.lendingBooks.containsKey(userID)) {
                throw new lendingNotFoundByUserID(userID);
            }
            lendingBookOnUser = Library.lendingBooks.get(userID);
            if (!lendingBookOnUser.containsKey(bookID)) {
                throw new lendingNotFoundByBookID(userID, bookID);
            }
            //увеливаем доступное кол-во книг в мапе books
            Library.books.get(bookID).availableCopies = Library.books.get(bookID).getAvailableCopies() + lendingBookOnUser.get(bookID);
            //вносим информацию в возвращаенных книгах в мап lendingBooks
            lendingBookOnUser.remove(bookID);
            if (lendingBookOnUser.size() == 0) {
                Library.lendingBooks.remove(userID);
            }
            else {
                Library.lendingBooks.put(userID, lendingBookOnUser);
            }
            System.out.printf("Книга %s, вернули\n", Library.books.get(bookID).getTitle());
        } catch (userNotFoundByID e) {
            System.out.println(e.getMessage());
        }
        catch (bookNotFoundByID e) {
            System.out.println(e.getMessage());
        }
        catch (lendingNotFoundByUserID e) {
            System.out.println(e.getMessage());
        }
        catch (lendingNotFoundByBookID e) {
            System.out.println(e.getMessage());
        }
    }
}