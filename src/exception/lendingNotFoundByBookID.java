package exception;

public class lendingNotFoundByBookID extends Exception {
    public lendingNotFoundByBookID(int UserID, int BookID) {
        super("Не найдены выданные книги по читателю с ID " + UserID + " и книги с ID " + BookID);
    }
}