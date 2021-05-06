package View.Exceptions;

public class InvalidCardAddressException extends Exception{
    public InvalidCardAddressException() {
        super("there is no monster on one of these addresses");
    }
}
