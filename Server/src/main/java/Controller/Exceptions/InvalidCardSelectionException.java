package Controller.Exceptions;

public class InvalidCardSelectionException extends Exception {
    public InvalidCardSelectionException(){
        super("invalid selection");
    }
}
