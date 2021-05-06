package View.Exceptions;

public class InvalidFlipSummonException extends Exception{
    public InvalidFlipSummonException() {
        super("you can’t flip summon this card");
    }
}
