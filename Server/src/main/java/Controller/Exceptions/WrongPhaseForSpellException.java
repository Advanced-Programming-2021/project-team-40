package Controller.Exceptions;

public class WrongPhaseForSpellException extends Exception{
    public WrongPhaseForSpellException() {
        super("you can't activate an effect in this phase");
    }
}
