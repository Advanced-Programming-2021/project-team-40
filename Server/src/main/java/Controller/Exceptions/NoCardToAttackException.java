package Controller.Exceptions;

public class NoCardToAttackException extends Exception{
    public NoCardToAttackException(){
        super("there is no card to attack here");
    }
}
