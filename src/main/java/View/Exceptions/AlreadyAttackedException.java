package View.Exceptions;

public class AlreadyAttackedException extends Exception{
    public AlreadyAttackedException() {
        super("this card already attacked");
    }
}
