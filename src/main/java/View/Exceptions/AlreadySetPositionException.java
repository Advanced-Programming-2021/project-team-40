package main.java.View.Exceptions;

public class AlreadySetPositionException extends Exception{
    public AlreadySetPositionException(){
        super("you already changed this card position in this turn");
    }
}
