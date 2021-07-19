package Controller.Exceptions;

public class InvalidActivateException extends Exception{
    public InvalidActivateException() {
        super("activate effect is only for spell cards");
    }
}
