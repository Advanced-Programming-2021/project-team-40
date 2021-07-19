package Controller.Exceptions;

public class MaxedOutCardInDeckException extends Exception {
    public MaxedOutCardInDeckException(String cardName, String deckName) {
        super("there are already three cards with name " + cardName + " in deck " + deckName);
    }
}
