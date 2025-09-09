package exception;

public class userNotFoundByID extends Exception {
    public userNotFoundByID(int userID) {
        super("Не найден читатель по ID " + userID);
    }
}