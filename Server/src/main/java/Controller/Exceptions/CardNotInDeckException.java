package Controller.Exceptions;

public class CardNotInDeckException extends Exception{
    public CardNotInDeckException(String deck, String cardName) {
        super("card with name " + cardName + " does not exist in " + deck + " deck");
    }
}
