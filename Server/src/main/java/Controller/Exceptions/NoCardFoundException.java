package Controller.Exceptions;

public class NoCardFoundException extends Exception{
    public NoCardFoundException(){
        super("no card found in the given position");
    }
}
