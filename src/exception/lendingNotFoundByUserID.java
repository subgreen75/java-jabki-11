package exception;

public class lendingNotFoundByUserID extends Exception {
    public lendingNotFoundByUserID(int UserID) {
        super("Не найдены выданные книги по читателю с ID " + UserID);
    }
}