package View.Exceptions;

public class DirectAttackNotPossibleException extends Exception{
    public DirectAttackNotPossibleException(){
        super("you can't attack the opponent directly");
    }
}
