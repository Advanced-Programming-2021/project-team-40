package View.Exceptions;

public class WrongPhaseForSpellException extends Exception{
    public WrongPhaseForSpellException() {
        super("you can't activate an effect on this turn");
    }
}
