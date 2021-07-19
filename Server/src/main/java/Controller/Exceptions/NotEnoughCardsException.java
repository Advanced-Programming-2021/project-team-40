package Controller.Exceptions;

public class NotEnoughCardsException extends Exception{
    public NotEnoughCardsException() {
        super("there are not enough cards for tribute");
    }
}
