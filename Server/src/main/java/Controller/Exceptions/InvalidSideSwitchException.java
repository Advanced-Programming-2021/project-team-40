package Controller.Exceptions;

public class InvalidSideSwitchException extends Exception {
    public InvalidSideSwitchException() {
        super("invalid numbers entered for side or main deck");
    }
}
