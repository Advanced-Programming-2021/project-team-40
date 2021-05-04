package View.Exceptions;

public class InvalidChangePositionException extends Exception{
    public InvalidChangePositionException(){
        super("you can’t change this card's position");
    }
}
