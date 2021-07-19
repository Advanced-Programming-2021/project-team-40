package Controller.Exceptions;

public class AlreadySetPositionException extends Exception{
    public AlreadySetPositionException(){
        super("you already changed this card's position in this turn");
    }
}
