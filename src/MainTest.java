import model.Book;
import model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.Library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MainTest {
    @Test
    // проверка добавление книги
    void testAddBook() {
        Library.addBook("Какая то книга", "Какой то автор", 2005, 1, false);
        Library.displayBooks(Library.books);
        // добавим с пустым названием
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.addBook("", "Какой то автор", 2005, 1, false));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.addBook("Книга 2", "", 2005, 1, false));

    }

    //проверка добавления читателя
    @Test
    void testAddUser() {
        Library.addUser("Читатель1","1@1.ru", false);
        // добавим с пустым названием
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.addUser("","1@1.ru", false));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.addUser("Какой то читатель","", false));
        Library.displayUsers(Library.users);
    }

    //проверка поиска книги
    @Test
    void testFindBook() {
        HashMap<Integer, Book> findBooks;
        System.out.println("Весь список книг:");
        Library.displayBooks(Library.books);

        System.out.println("проверка на \"НЕ НАШЛОСЬ\":");
        //проверка на "НЕ НАШЛОСЬ"
        findBooks = Library.getBooks(0, "НАЗВАНИЕ", "АВТОР", 1991);
        Library.displayBooks(findBooks);
        Set<Integer> expectedBooks = new HashSet<>();
        Assertions.assertArrayEquals(expectedBooks.toArray(), findBooks.keySet().toArray());

        System.out.println("проверка на \"НАШЛОСЬ\":");
        //проверка на "НАШЛОСЬ" сравниваем список id
        expectedBooks.add(1);
        findBooks = Library.getBooks(0, "КНИГА", "АВТОР", 2005);
        Library.displayBooks(findBooks);
        Assertions.assertArrayEquals(expectedBooks.toArray(), findBooks.keySet().toArray());
    }

    //проверка поиска читателя
    @Test
    void testFindUser()  {
        HashMap<Integer, User> usersFind;
        Set<Integer> expectedUsers = new HashSet<>();
        System.out.println("Весь список читателей:");
        Library.displayUsers(Library.users);

        System.out.println("");
        System.out.println("Ищем по пустым параметрам (по идее должен найти все)");
        usersFind = Library.getUsers(0, null, null);
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ID = 1");
        usersFind = Library.getUsers(1, null, null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ФИО =  Читатель1");
        usersFind = Library.getUsers(0, "Читатель1", null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ID = 1 и ФИО =  Читатель1");
        usersFind = Library.getUsers(1, "Читатель1", null);
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по ФИО =  Читатель2 (найтись не должно)");
        usersFind = Library.getUsers(0, "Читатель2", null);
        expectedUsers.clear();
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);

        System.out.println("");
        System.out.println("Ищем по емейл like ru");
        usersFind = Library.getUsers(0, "", "ru");
        expectedUsers.clear();
        expectedUsers.add(1);
        Assertions.assertArrayEquals(expectedUsers.toArray(), usersFind.keySet().toArray());
        Library.displayUsers(usersFind);
    }

    //проверка выдачи и возврата книг
    @Test
    void testLendingBook()  {
        Library.displayBooks(Library.books);
        System.out.println("выдали книгу 1");
        Library.lendingBook(1, 1, 1);
        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 0);
        System.out.println("еще раз выдали книгу 1");
        Library.lendingBook(1, 1, 1);
        //проверим на ошибки при неправильном параметре
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.lendingBook(1, 1, 0));
        //проверим вывод выданных книг
        Library.displayLendingBooks();
        System.out.println("вернули книгу 1");
        Library.returnBook(1,1);
        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 1);
        Library.displayLendingBooks();
        Library.displayBooks(Library.books);
        System.out.println("вернули книгу 1");
        Library.returnBook(1,1);
        //сравним доступное
        Assertions.assertEquals(Library.books.get(1).getAvailableCopies(), 1);
        System.out.println("вернули книгу 2 по читателю 2");
        Library.returnBook(2,2);

    }
}