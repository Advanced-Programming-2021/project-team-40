package View.Exceptions;

public class WrongPhaseException extends Exception{
    public WrongPhaseException(){
        super("action not allowed in this phase");
    }
}
