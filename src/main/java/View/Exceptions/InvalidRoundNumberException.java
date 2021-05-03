package main.java.View.Exceptions;

public class InvalidRoundNumberException extends Exception{
    public InvalidRoundNumberException(){
        super("number of rounds is not supported");
    }
}
