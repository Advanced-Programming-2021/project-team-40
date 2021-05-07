package View.Exceptions;

public class AlreadyActivatedException extends Exception {
    public AlreadyActivatedException() {
        super("you have already activated this card");
    }
}
