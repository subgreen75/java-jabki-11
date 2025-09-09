package exception;

public class bookNotFoundByID extends Exception {
    public bookNotFoundByID(int bookID) {
        super("Не найдена книга по ID " + bookID);
    }
}