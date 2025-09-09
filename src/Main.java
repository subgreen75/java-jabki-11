import model.Book;
import model.User;
import service.Library;
import ui.ConsoleMenu;

import java.util.HashMap;

public class Main {


    public static void main(String[] args) {



        Library.init();
        /*
        Library.displayBooks(Library.books);
        HashMap<Integer, Book> booksFind;
        booksFind = Library.getBooks(1,null,null,0);
        Library.displayBooks(booksFind);
        booksFind = Library.getBooks(0,"ВИ",null,0);
        Library.displayBooks(booksFind);
        booksFind = Library.getBooks(0,"","Толс",0);
        Library.displayBooks(booksFind);

        HashMap<Integer, User> usersFind;
        usersFind = Library.getUsers(0,null,"mail");
        Library.displayUsers(usersFind);
*/
        ConsoleMenu.start();
    }


}