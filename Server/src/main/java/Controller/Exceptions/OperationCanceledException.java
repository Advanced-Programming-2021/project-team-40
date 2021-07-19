package Controller.Exceptions;

public class OperationCanceledException extends Exception{
    public OperationCanceledException() {
        super("Operation canceled!");
    }
}
