package View.Exceptions;

public class AttackNotPossibleException extends Exception{
    public AttackNotPossibleException(){
        super("you can’t attack with this card");
    }
}
