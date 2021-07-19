package Controller.Exceptions;

public class InvalidSummonException extends Exception{
    public InvalidSummonException(){
        super("you can’t summon this card");
    }
}
