package Controller.Exceptions;

public class InvalidDeckNameException extends Exception {
    public InvalidDeckNameException(String deckName){
        super("deck with name "+ deckName +" does not exist");
    }
}
