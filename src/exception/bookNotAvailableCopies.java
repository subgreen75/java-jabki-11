package exception;

public class bookNotAvailableCopies extends Exception {
    public bookNotAvailableCopies(String title, int copies) {
        super("Книги " + title + " нет в наличии, доступно " + copies + " экземпляров");
    }
}