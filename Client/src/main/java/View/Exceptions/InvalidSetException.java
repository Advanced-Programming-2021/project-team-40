package View.Exceptions;

public class InvalidSetException extends Exception {
    public InvalidSetException() {
        super("you can’t set this card");
    }
}
