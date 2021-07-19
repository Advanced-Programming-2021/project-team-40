package View.Exceptions;

public class AlreadyInPositionException extends Exception{
    public AlreadyInPositionException(){
        super("this card is already in the wanted position");
    }
}
