import model.Book;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Library;

import java.util.HashMap;

class MainTest {
    @Test
    // проверка добавление книги
    void testAddBook() {
        Library.addBook("Какая то книга", "Какой то автор", 2005, 1);
        Library.displayBooks(Library.books);
        // добавим с пустым названием
        Library.addBook("", "Какой то автор", 2005, 1);
        Library.displayBooks(Library.books);
        Library.addBook("Книга 2", "", 2005, 1);
        Library.displayBooks(Library.books);
    }

    //проверка добавления читателя
    @Test
    void testAddUser() {
        Library.addUser("Читатель1","1@1.ru");
        Library.displayUsers(Library.users);
        // добавим с пустым названием
        Library.addUser("","1@1.ru");
        Library.displayUsers(Library.users);
    }

    //проверка поиска книги
    @Test
    void testFindBook() {
        HashMap<Integer, Book> booksFind;
        booksFind = Library.getBooks(0, "НАЗВАНИЕ", "АВТОР", 1991);
        Library.displayBooks(booksFind);
        booksFind = Library.getBooks(2, "Название2", "Автор2", 1991);
        Library.displayBooks(booksFind);
    }

    //проверка поиска читателя
    @Test
    void testFindUser()  {
        HashMap<Integer, User> usersFind;
        usersFind = Library.getUsers(0, null, null);
        Library.displayUsers(usersFind);
        usersFind = Library.getUsers(1, null, null);
        Library.displayUsers(usersFind);
        usersFind = Library.getUsers(0, "Читатель1", null);
        Library.displayUsers(usersFind);
        usersFind = Library.getUsers(1, "Читатель1", null);
        Library.displayUsers(usersFind);
        usersFind = Library.getUsers(0, "Читатель2", null);
        Library.displayUsers(usersFind);
        usersFind = Library.getUsers(0, "", "ru");
        Library.displayUsers(usersFind);
    }

    //проверка выдачи и возврата книг
    @Test
    void testLendingBook()  {
        Library.displayBooks(Library.books);
        System.out.println("выдали книгу 1");
        Book.lendingBook(1, 1, 1);
        System.out.println("еще раз выдали книгу 1");
        Book.lendingBook(1, 1, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Book.lendingBook(1, 1, 0));
        Library.displayLendingBooks();
        System.out.println("вернули книгу 1");
        Book.returnBook(1,1);
        Library.displayLendingBooks();
        Library.displayBooks(Library.books);
        System.out.println("вернули книгу 1");
        Book.returnBook(1,1);
        System.out.println("вернули книгу 2 по читателю 2");
        Book.returnBook(2,2);
    }
}