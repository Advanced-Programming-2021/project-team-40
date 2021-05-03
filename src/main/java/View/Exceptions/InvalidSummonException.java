package main.java.View.Exceptions;

public class InvalidSummonException extends Exception{
    public InvalidSummonException(){
        super("you can’t summon this card");
    }
}
