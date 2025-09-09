package exception;

public class bookReturnGreaterThanAllowed extends Exception {
    public bookReturnGreaterThanAllowed() {
        super("Попытка вернуть лишние книги");
    }
}