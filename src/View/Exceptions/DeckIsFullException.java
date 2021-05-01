package View.Exceptions;

public class DeckIsFullException extends Exception {
    public DeckIsFullException(String deckPart){
        super(deckPart + " deck is full");
    }
}
