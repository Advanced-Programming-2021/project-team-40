package View.Exceptions;

public class SameUserChosenException extends Exception {
    public SameUserChosenException() {
        super("you must choose another player to start game");
    }
}
