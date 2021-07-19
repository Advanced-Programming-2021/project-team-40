package Controller.Exceptions;

public class CardCantDiscardItselfException extends Exception{
    public CardCantDiscardItselfException(){
        super("card can't discard itself exception");
    }
}
