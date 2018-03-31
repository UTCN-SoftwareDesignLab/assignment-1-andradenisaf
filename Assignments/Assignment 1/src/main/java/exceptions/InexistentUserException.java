package exceptions;

public class InexistentUserException extends Exception {

    public InexistentUserException(String msg) {
        super(msg);
    }
}
