package main.java.View.Exceptions;

public class NoCardFoundException extends Exception{
    public NoCardFoundException(){
        super("no card found in the given position");
    }
}
