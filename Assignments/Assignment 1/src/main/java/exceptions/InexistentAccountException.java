package exceptions;

public class InexistentAccountException extends Exception {

    public InexistentAccountException(String msg) {
        super(msg);
    }
}
