package main.java.View.Exceptions;

public class WrongPhaseException extends Exception{
    public WrongPhaseException(){
        super("action not allowed in this phase");
    }
}
